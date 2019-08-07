README.md

# Docker

```
docker run -d --name shortbin -p <port>:8080 -v <storage_path>:/opt/shortbin/storage -v <config_path>:/opt/shortbin/config shortbin
```

# Docker compose
Edit docker-compose.yml and modify:
- traefik.frontend.rule
- volumes

```
docker-compose up -d 
```

When the container starts, it will put a default SQLite database (shortbin.db) and application.yml in the configuration directory if it doesn't exist.

# Credits
Word list obtained from https://github.com/first20hours/google-10000-english