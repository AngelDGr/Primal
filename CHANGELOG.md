# 1.1.6 - 1.21
# Additions
- Completely removed GeckoLib dependency! A lot of animation-related stuff should work a lot better now
- Added flick animation to snakes while on the player's neck
- Added a new config option to add support for more wolf variants
- Added look animation to deer

# Changes
- Snakes don't get out of its owner neck when flying on creative
- Changed crocodile model
- Added new animation when brushed a crocodile scute
- Modified logic for lion sigma variant to be just a texture change instead of a variant change

# Fixes
- Fixed baby rabbits not being assigned the custom primal variant correctly when born from breeding

# 1.1.5 - 1.21
# Additions
- Added recipe integration with EMI for helmet decorations

# Changes
- Changed logic for helmet decorations from an enum to a custom registry, making a lot easier for other mods to add their own helmet decorations
- Made babies from tamed parents being tamed automatically
- Made snakes being able to dance with the instrument game event (emitted by goat horns, conch shells and probably other modded instruments)
- Improved eagle idle movement on enclosed spaces

# Fixes
- Fixed paint and eye layer not applying correctly to baby lions if the custom baby model is off
- Made tamed pets unable to attack pets with the same owner if alerted by others
- Fixed pets ignoring the vanilla sitting state, causing incompatibilities with other mods
- Modify wolf remodel animations logic to work with snowy spirit sleds

# 1.1.4 - 1.21
# Additions
- Added optional built-in resource packs to give banner patterns a texture similar to the vanilla ones from 1.21.2+
- Added optional built-in resource packs to give spawn eggs a texture similar to the vanilla ones from 1.21.5+
- Added plants description for fieldguide mod

# Changes
- Made banner patterns unstackable like vanilla ones

# Fixes
- Fixed tamed adult eagles no rendering the band on its leg
- Fixed weird positioning on snake when carrying it with Carry On mod (and possible related issues)
- Added missing translation for when the deer eats
- Fixed dreamcatcher effects sometimes not applying on beds in some positions

# 1.1.3 - 1.21
# Additions
- Added textures and description for the placeholder item used to spawn snakes on chests. It should be visible with Emi-Loot or similar

# Changes
- Updated Spanish translations

# Fixes
- Fixed a crash with walrus whirlwind attack
- Fixed "invalid player data" when entering in a world with Emi-Loot installed
- Fixed issues with cattails and river reeds growing
- Fixed marine snake variant don't being applied on beaches and oceans

# 1.1.2 - 1.21
# Additions
- Added support for pet cemetery skeleton and zombie wolves if the wolf remodel is active
- Added lion_spawn_on tag that contains snow, grass and coarse dirt

# Changes
- Improved mob pictures on fieldguide mod
- Made cave lion variant be applied on any snowy biome

# Fixes
- Fixed broken snake pictures on fieldguide
- Fixed broken plant pictures on fieldguide mod
- Removed unused shark variation

# 1.1.1 - 1.21
# Additions
- Made thorny acacia logs strippable into the normal acacia ones

# Changes
- Bear swipe can't longer damage other pets from the same owner
- Made able to remove snake tribal marks
- Reduced lion walking animation speed
- Made deer don't be scared or stare to players on creative mode
- Made manned lion roar unable to command tamed lions, unless it is tamed too and with the same owner
- Changed thorny acacia spawn logic, now it should work with any datapack

# Fixes
- Fixed an issue when generating hollow logs
- Fixed an issue with the polar bear air supply
- Fixed an issue with applying the new reflected damage attribute too early
- Fixed an issue when growing river reeds and cattails
- Fixed a probable issue related to spawning snakes on chests

# 1.1.0 - 1.21
# Additions
- - Added the Cassowary!
- - Added the Walrus!
- - Added the Lion!
- - Added the Snake!
- - Added the Deer, surprise!
- - Added rabbit remodel, changing the hitbox from vanilla 0.4 x 0.5 to 0.55 x 0.7 to fit the model, and new variants (gray and sand)
- - Added wolf remodel, changing the hitbox from vanilla 0.6 x 0.5 to 1.0 x 1.15 to fit the model, with support for wolf variants from No Man's Land, Atmospheric and Environmental
- - Added dolphin remodel and new variants (lukewarm, cold, warm and a retexture of No Man's Land river variant)
- - Added fox re-remodel, changing the hitbox from vanilla 0.6 x 0.7 to 0.6 x 0.85
- Added new mackerel shark variant on cold oceans and sleeper shark variant on frozen oceans
- Added new temperate seashells and cold seashells
- Added cold_seashells and temperate_seashells biome tags to determine in which biomes the new seashells spawn
- Added new seashells item tag, it contains all seashells + No Man's Land seashells
- Added new creative menu with custom logo
- Added new custom baby models for bears and crocodiles!
- Added thorned effect
- Added exotic fruits
- Now the polar bear and normal bear movement will be improved in water, they can swim to being able to get submerged prey!
- Polar bears now have 1.5-step height instead of just 0.6
- Added conch shells, powerful items that let you teleport your pets! Works even if the pet is in another dimension or in an unloaded chunk!
- Added cattails, they generate in swamps instead of river reeds, and with 33% probability in river reeds patches
- Added dried straw block set
- Added thorned acacia and exploseed
- Added chomp trap
- Added hollow logs, they generate around the world with a probability of containing snakes
- Added 13+ new advancements

# Changes
- Now crocodiles will also eat any item that drops the player on death
- Improved move control for eagles, making them a lot faster and cooler
- Iron Golems now attack crocodiles and bears that are targeting villagers
- Tamed Bears now run when the owner gets too far away
- Modified shark model
- Modified shark hitbox from 1.5x1.5 to 1.75x1.30
- Modified bear model
- Modified crocodile black and brown textures
- Modified heavy effect texture
- Modified shark tooth texture
- Modified eagle model, including bigger wings when flying
- Modified seashells block texture
- Modified nest model and texture
- Tamed Eagles can teleport midair when following its owner
- Eagle nests can generate to replace replaceable blocks, like snow layers
- Added river reeds retexture
- Added is_river tag to river reeds spawning
- The nest will now also open a connection when there’s a full block on that side
- Modified nearest important block sensor to only detect each 10 ticks, to avoid too much stress on the server
- Increased crocodile speed on water, and now progressively slow down as they approach the water surface, helping them remain slightly submerged instead of rising too high.
- Modified slightly shadow of the baby fox
- Added a 3s cooldown between crocodile thrashes
- Added salmon to entity tag bear_huntable
- Changed name of the seashells from "Seashells" to "Warm Seashells"
- Improved eagles sight, they will see prey from farther away (from 16 to 48 blocks)
- Eagles will work better underwater
- Improved eagles free movement, they should be more in the air
- Eagles now will be scared only from entities sprinting
- If an eagle has an egg on its home nest, it will try to remain closer
- When thrashed by a crocodile, it will show the hunger bar correctly
- When grabbed by an eagle, it will show the hunger bar correctly
- Modified growth cycle of river reeds, they now can be three block tall on land too!
- Increase river reed probability of naturally generating with a puff from 40% to 50%
- River reeds can't naturally age anymore, to avoid all of them having yellow puffs (they still can grow into a three block)
- Added is_forest tag to bear spawn
- Villagers aren't longer scared of tamed bears unless they are actively damaging villagers or damaged villagers recently
- Added straw block to the mineable_with_hoe_tag
- Added new block tag creates_wide_smoke, that include the straw bales
- Added the straw bale to compostables
- Reduced default spawn weight of shark group from 5 to 2 
- Improved water rotations for mobs
- Added a glowing eyes to sharks when they are in a conduit trance
- Modified bear hitbox when sleeping/sitting
- Removed "do nothing" task from shark AI, they will move more now
- Modified biome tags path, to be more organized
- Increased eagle attack range, so they can attack from farther away
- Decreased eagle hitbox, so they don't get hit too often
- Eagles now have a 33% of doing damage the moment they grab an entity
- Seashells are snowloggable now
- Decreased single bear default spawn rate to 15

# Fixes
- Made the straw bale strips be consistent with the vanilla hay bale (biggest fix in primal fr)
- Polar Bear and Bears won't consume the entire bucket when breded
- Baby crocodiles spawned by right-clicking an adult with a spawn egg now have the correct biome variant set
- Fixed baby polar bear shadow being too large
- Eagles shouldn't be able to attack pets with the same owner
- Drowneds can't longer be bear jockeys
- River reeds can't be placed if there's no enough air/water blocks above
- Tamed bears can't longer attack pets from the same owner
- Now when removed an egg that isn't fully hatched from a nest it resets the hatch state properly
- The unstuck animation for foxes now triggers correctly
- Fixed being unable to feed knock out bears

#### And a lot of other things, honestly I lost count of the amount of changes, it was a lot
#### - Tenebris Mors

# 1.0.7 - 1.21
# Changes
- Modified nest logic, now it works with modded eggs if added on configuration! You only need to specify both the egg block and the entity that it will hatch from it
- Added a new config option to add placeable eggs into the nest without modifying the animal_egg tag

# Fixes
- Fixed baby shadows being too large

# 1.0.6 - 1.21
# Additions
- Added spanish translation! (Ahuevo)
- Added brazilian translation!
- Added russian translation!
- Added pirate speaking translation! (Arrrr)
- Added bear_healing_treats item tag, it contains sweet berries, glow berries, sugar, cookies and pumpkin pie
- Added explosive crocodiles
- Added crocodile_explosive item tag, for items that makes a crocodile explode
- Added arid and humid variants of crocodile blocks, made with mud for the humid one and sand for the arid one.
- Added stone cutting recipes for the crocodile blocks
- Added crocodile_tick_tock item tag, for items that makes a crocodile a tick-tock croc
- Polar bear health increased from 30 to 60, with a config option to disable it
- Added the ability to remove the barrel from a bear by interacting with it using shears while its inventory is empty

# Changes
- Fox run animation triggers depending on the fox speed, instead of triggering just when the fox is aggressive
- Increased bear roar volume from 1 to 4, it should be heard from farther away
- Decreased bear health from 80 to 50
- Increased bear base speed from 0.1 to 0.14
- The Bear now can be healed with any item from bear_healing_treats, the healing amount is 1+half the nutrition of the food

# Fixes
- Sharks no longer attack drowneds mounting other sharks
- Animations for replaced mobs no longer trigger on the server, it should fix some crashes
- Changed all activity names to have the suffix primal_ to avoid incompatibilities with other mods
- Fixed chicken jockey able to spawn after a bear or shark jockey spawn
- Fixed bears and eagles being commandable by non-owners

# 1.0.5 - 1.21
# Additions
- Now you can use biome tags inside the config, just put a # before to be recognized as tag

# Fixes
- Fixed the arrow recipe with shark tooth replacing the vanilla one

# 1.0.4 - 1.21
### Additions
- Added a config file! You can control what mobs spawn, spawn weights, group size, alongside being able to config world features like river reeds and seashells
- Bears can spawn with a baby, like vanilla Polar Bears, it is rarer than a normal spawn

# Changes
- Sharks no longer attack other low health sharks
- Added a 10s cooldown to the bear between each roar, so it doesn't trigger continuously when hunting groups
- Added a 1.5 block step up to the bear so doesn't get stuck in blocks

# 1.0.3 - 1.21
### Additions
- Added built-in support for No Man's Land mobs and biomes
- Added new exclusive textures for No Man's Land fox variants!

# 1.0.2 - 1.21
### Additions
- Added support for No Man's Land fox variants!

# 1.0.1 - 1.21
### Fixes
- Fix an issue when starting the mod on servers

# 1.0.0 - 1.21
### Additions
- Initial Release