#!/usr/bin/env sh

if test -f /secrets/srvetterlatte/username;
then
    export SERVICEUSER_USERNAME=$(cat /secrets/srvetterlatte/username)
fi

if test -f /secrets/srvetterlatte/password;
then
    export SERVICEUSER_PASSWORD=$(cat /secrets/srvetterlatte/password)
fi
