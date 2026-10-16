# Diet Revived

Unofficial port of [Diet](https://github.com/illusivesoulworks/diet) by C4 to Minecraft 1.21.1 (NeoForge and Fabric).

Diet tracks five food groups per player (fruits, grains, proteins, sugars, vegetables). Eating raises the groups the food belongs to, hunger slowly drains them, and keeping a balanced diet gives bonuses. Groups, effects and food values are all configurable through datapacks and the server config.

The mod id (`diet`), the API package and the datapack format are the same as the original, so existing addons, datapacks and configs keep working.

## Building

```
./gradlew buildAll       # both loaders
./gradlew gameTestAll    # runs the game tests on both loaders
```

## License

LGPL-3.0-or-later, same as the original. See `COPYING` and `COPYING.LESSER`.
