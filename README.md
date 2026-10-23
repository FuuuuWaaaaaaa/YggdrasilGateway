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




## Docker (GitHub Container Registry)

The `main` branch image is published as `latest`; version tags are published with the matching tag. Pull and run it with:

```sh
docker pull ghcr.io/fuuuuwaaaaaaa/yggdrasilgateway:latest
docker run -d --name yggdrasilgateway \
  -p 8080:8080 \
  -v "$PWD/data:/app/run/data" \
  -v "$PWD/config:/app/run/config" \
  --restart unless-stopped \
  ghcr.io/fuuuuwaaaaaaa/yggdrasilgateway:latest
```

Back up the `data` directory before upgrades. Configure the database and HTTP listener in the generated properties under `config/`. On first startup, a random administrator password is saved to `data/initial-admin-password.txt`; sign in as `admin`, change it, then delete that file. The image is published by this fork's GitHub Actions after a successful build; the first image becomes available after that workflow completes. GitHub Container Registry packages start private unless you change the package visibility; make the package public to allow unauthenticated production pulls, or authenticate with `docker login ghcr.io`.
