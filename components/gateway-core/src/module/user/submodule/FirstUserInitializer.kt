package com.kasukusakura.yggdrasilgateway.core.module.user.submodule

import com.kasukusakura.yggdrasilgateway.api.eventbus.EventPriority
import com.kasukusakura.yggdrasilgateway.api.eventbus.EventSubscriber
import com.kasukusakura.yggdrasilgateway.core.database.DatabaseConnectionManager.mysqlDatabase
import com.kasukusakura.yggdrasilgateway.core.event.DatabaseInitializationEvent
import com.kasukusakura.yggdrasilgateway.core.module.user.db.UserRoleTable
import com.kasukusakura.yggdrasilgateway.core.module.user.db.UsersTable
import org.ktorm.dsl.*
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.PosixFilePermission
import java.nio.file.attribute.PosixFilePermissions
import java.security.SecureRandom
import java.util.Base64
import java.util.EnumSet

@EventSubscriber
internal object FirstUserInitializer {
    private val log = LoggerFactory.getLogger(FirstUserInitializer::class.java)

    @EventSubscriber.Handler(priority = EventPriority.HIGH)
    fun DatabaseInitializationEvent.handle() {
        mysqlDatabase.useTransaction { trans ->
            trans.connection.autoCommit = false

            val result = mysqlDatabase.from(UsersTable)
                .select(count())
                .rowSet
            result.next()
            if (result.getInt(1) == 0) {
                log.info("Creating First User...")

                val initialPassword = ByteArray(32).also(SecureRandom()::nextBytes)
                    .let { Base64.getUrlEncoder().withoutPadding().encodeToString(it) }
                val passwordSalt = ByteArray(32).also(SecureRandom()::nextBytes)
                writeInitialPassword(initialPassword)

                mysqlDatabase.insert(UsersTable) {
                    set(UsersTable.username, "admin")
                    set(UsersTable.email, "admin@localhost")
                    set(
                        UsersTable.password,
                        PasswordHasher.hashPassword(initialPassword.toByteArray(), passwordSalt)
                    )
                    set(UsersTable.passwordSalt, passwordSalt)
                    set(UsersTable.creationTime, System.currentTimeMillis())
                }

                val uid = mysqlDatabase.from(UsersTable)
                    .select(UsersTable.userid)
                    .where { UsersTable.username eq "admin" }
                    .rowSet.also { it.next() }[UsersTable.userid]


                mysqlDatabase.insert(UserRoleTable) {
                    set(UserRoleTable.userid, uid)
                    set(UserRoleTable.role, "yg-admin")
                }
                log.info("Initial admin password was written to data/initial-admin-password.txt")
            }
        }
    }

    private fun writeInitialPassword(password: String) {
        val path = Path.of("data", "initial-admin-password.txt")
        Files.createDirectories(path.parent)
        try {
            Files.createFile(
                path,
                PosixFilePermissions.asFileAttribute(
                    EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE)
                )
            )
        } catch (_: UnsupportedOperationException) {
            if (!Files.exists(path)) Files.createFile(path)
        } catch (_: java.nio.file.FileAlreadyExistsException) {
            // A stale bootstrap secret can exist if the database transaction failed earlier.
        }
        Files.writeString(path, password)
    }
}
