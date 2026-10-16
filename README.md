# Diet Reborn

Unofficial port of [Diet](https://github.com/illusivesoulworks/diet) by C4 to NeoForge 1.21.1.

Diet tracks five food groups per player (fruits, grains, proteins, sugars, vegetables). Eating raises the groups the food belongs to, hunger slowly drains them, and keeping a balanced diet gives bonuses. Groups, effects and food values are all configurable through datapacks and the server config.

The mod id (`diet`), the API package and the datapack format are the same as the original, so existing addons, datapacks and configs keep working.

## Building

```
./gradlew build          # also runs the game tests
./gradlew runClientSmoke # opens a world and the diet screen, then quits
```

## License

LGPL-3.0-or-later, same as the original. See `COPYING` and `COPYING.LESSER`.
