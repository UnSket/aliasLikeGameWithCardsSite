# Nginx conf for jelastic

```
location ~^/api/(.*)$ {
    add_header Allow "GET, POST, DELETE, PATCH" always;


    if ($cookie_SRVGROUP ~ group|common) {
            proxy_pass http://$cookie_SRVGROUP;
            error_page   500 502 503 504 = @rescue;
    }

    if ($cookie_SRVGROUP !~ group|common) {
            add_header Set-Cookie "SRVGROUP=$group; path=/";
    }
    proxy_pass http://default_upstream;
    add_header Set-Cookie "SRVGROUP=$group; path=/";
}

location ~^(.*)$ {
       root /etc/nginx/conf.d/frontend;
       try_files $uri $uri/ /index.html;
}
```

Files config locations - `/home/jelastic/conf/`

