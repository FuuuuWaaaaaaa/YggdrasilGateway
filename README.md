# Yggdrasil Gateway

------

> [!WARNING]
> 此项目目前处于早期开发阶段，如果需要对此系列软件进行升级，请先进行数据备份！


Yggdrasil Gateway 是一款旨于实现正版和外置登录共存的支持，用于连接两个或多个外置验证服务器下的玩家，使他们能够在同一个服务器上一起游戏。

> [!NOTE]
> Yggdrasil Gateway 只是一个 HTTP 服务器，只能提供登录验证服务。
> 
> 高级拒绝信息、聊天签名重设 等功能需要额外安装插件以得到功能支持。
>
> 另见 [YggdrasilGateway/GatewayEnhancement](https://github.com/YggdrasilGateway/GatewayEnhancement)

## 安装

- 见 https://yggdrasilgateway.github.io/



## Docker 

`main` 分支的镜像会以 `latest` 发布；版本标签则以对应的标签名发布。拉取并运行它的命令如下：

```sh
docker pull ghcr.io/fuuuuwaaaaaaa/yggdrasilgateway:latest
docker run -d --name yggdrasilgateway \
  -p 8080:8080 \
  -v "$PWD/data:/app/run/data" \
  -v "$PWD/config:/app/run/config" \
  --restart unless-stopped \
  ghcr.io/fuuuuwaaaaaaa/yggdrasilgateway:latest
```

升级前请备份 `data` 目录。在 `config/` 下生成的属性文件中配置数据库和 HTTP 监听器。首次启动时，随机管理员密码会保存到 `data/initial-admin-password.txt`；请以 `admin` 身份登录，修改密码，然后删除该文件。
