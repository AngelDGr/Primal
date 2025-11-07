package org.primal.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.primal.Primal_Main;
import org.primal.block_entity.NestBlockEntity;
import org.primal.registry.Primal_BlockEntities;
import org.primal.registry.Primal_Sounds;
import org.primal.registry.Primal_Tags;
import org.primal.util.AnimalEgg;
import org.primal.util.VariantHolderPrimal;

import java.util.*;

public class NestBlock extends BaseEntityBlock {
    public static final MapCodec<NestBlock> CODEC = simpleCodec(NestBlock::new);
    public static final BooleanProperty HAS_EGG = BooleanProperty.create("has_egg");
    public static final IntegerProperty HATCH = BlockStateProperties.HATCH;

    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION =
            ImmutableMap.copyOf(Util.make(Maps.newEnumMap(Direction.class), p_55164_ -> {
                p_55164_.put(Direction.NORTH, NORTH);
                p_55164_.put(Direction.EAST, EAST);
                p_55164_.put(Direction.SOUTH, SOUTH);
                p_55164_.put(Direction.WEST, WEST);
            }));

    public NestBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(NORTH, true)
                        .setValue(EAST, true)
                        .setValue(SOUTH, true)
                        .setValue(WEST, true)

                        .setValue(HAS_EGG, false)
                        .setValue(HATCH, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, HAS_EGG, HATCH);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter blockgetter = context.getLevel();
        BlockPos blockpos = context.getClickedPos();

        return this.defaultBlockState()
                .setValue(NORTH, !blockgetter.getBlockState(blockpos.north()).is(this))
                .setValue(EAST, !blockgetter.getBlockState(blockpos.east()).is(this))
                .setValue(SOUTH, !blockgetter.getBlockState(blockpos.south()).is(this))
                .setValue(WEST, !blockgetter.getBlockState(blockpos.west()).is(this));
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {

        if(facing != Direction.DOWN && facing != Direction.UP){
            return facingState.is(this)
                    ? state.setValue(PROPERTY_BY_DIRECTION.get(facing), false)
                    : state.setValue(PROPERTY_BY_DIRECTION.get(facing), true);
        }

        return state;
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state

                .setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.NORTH)), state.getValue(NORTH))
                .setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.SOUTH)), state.getValue(SOUTH))
                .setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.EAST)), state.getValue(EAST))
                .setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.WEST)), state.getValue(WEST));
    }

    @Override
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state
                .setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.NORTH)), state.getValue(NORTH))
                .setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.SOUTH)), state.getValue(SOUTH))
                .setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.EAST)), state.getValue(EAST))
                .setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.WEST)), state.getValue(WEST));
    }

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        var eggBlockEntity=getBlockEntity(level, pos);
        if(state.getValue(HAS_EGG) && eggBlockEntity!=null && newState.getBlock().defaultBlockState().isAir()){
            ItemStack eggStack= new ItemStack(eggBlockEntity.getEgg().getItem(), eggBlockEntity.getEgg().getCount());
            popResource(level, pos , eggStack);
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected void spawnAfterBreak(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull ItemStack stack, boolean dropExperience) {}

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {

        //Remove
        if((player.isSecondaryUseActive() || stack.isEmpty()) && state.getValue(HAS_EGG)){
            return removeEgg(player, level, pos, state, true) ?
                    ItemInteractionResult.sidedSuccess(level.isClientSide) : ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        //Place
        else if(isItemPlaceable(stack)){
            return tryPlaceEgg(player, level, pos, state, stack) ? ItemInteractionResult.sidedSuccess(level.isClientSide)
                    : ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public static boolean tryPlaceEgg(@Nullable LivingEntity entity, Level level, BlockPos pos, BlockState state, ItemStack stack) {
        if (!state.getValue(HAS_EGG)) {
            if (!level.isClientSide) {
                placeEgg(entity, level, pos, state, stack, false);
            }

            return true;
        }
        else if(state.getValue(HAS_EGG)
                //If the block entity has the same stack
                && getBlockEntity(level, pos).getEgg().is(stack.getItem())
                //If the current amount +1 is less or equal to the max egg possible
                && getBlockEntity(level, pos).getEggsAmount()+1 <= getMaxEggAmount(stack)) {

            if (!level.isClientSide) {
                placeEgg(entity, level, pos, state, stack, true);
            }

            return true;
        } else {
            return false;
        }
    }

    private static boolean removeEgg(@Nullable LivingEntity entity, Level level, BlockPos pos, BlockState state, boolean pop) {
        if (level.getBlockEntity(pos) instanceof NestBlockEntity nestBlockEntity) {

            ItemStack eggStack= new ItemStack(nestBlockEntity.getEgg().getItem(), 1);

            nestBlockEntity.removeEgg(entity);

            if(nestBlockEntity.getEgg().isEmpty()){
                BlockState blockstate = state.setValue(HAS_EGG, false);
                level.setBlock(pos, blockstate, 3);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, blockstate));
            }

            if(pop)
                popResource(level, pos, eggStack);

            return true;
        }

        return false;
    }

    private static void placeEgg(@Nullable LivingEntity entity, Level level, BlockPos pos, BlockState state, ItemStack stack, boolean addToEgg) {
        if (level.getBlockEntity(pos) instanceof NestBlockEntity nestBlockEntity) {

            if(addToEgg){
                nestBlockEntity.addEgg(stack.consumeAndReturn(1, entity), entity);
            }
            else {
                nestBlockEntity.setEgg(stack.consumeAndReturn(1, entity), entity);

                BlockState blockstate = state.setValue(HAS_EGG, true);
                level.setBlock(pos, blockstate, 3);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, blockstate));
            }

            level.playSound(null, pos, Primal_Sounds.PLACES_EGG.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    public boolean isItemPlaceable(ItemStack stack){
        return stack.getItem() instanceof BlockItem blockItem &&
                (blockItem.getBlock().defaultBlockState().is(Primal_Tags.Block.IS_ANIMAL_EGG) || Primal_Main.COMMON_CONFIG.extraPlaceableEggs.get().contains(BuiltInRegistries.BLOCK.getKey(blockItem.getBlock()).toString()));
    }

    public static int getMaxEggAmount(ItemStack eggStack) {
        if (eggStack.getItem() instanceof BlockItem blockItem) {
            Block eggBlock = blockItem.getBlock();

            Optional<Property<?>> eggPropertyOpt = eggBlock.defaultBlockState().getProperties().stream()
                    .filter(property -> property.getName().equals("eggs") && property instanceof IntegerProperty)
                    .findFirst();

            if (eggPropertyOpt.isPresent()) {
                IntegerProperty property = (IntegerProperty) eggPropertyOpt.get();
                return property.getPossibleValues().stream().max(Integer::compare).orElse(1);
            }
        }

        return 1; // fallback if no property found
    }

    public static Optional<BlockState> getEggBlockState(ItemStack eggStack){
        if(eggStack.getItem() instanceof BlockItem blockItem) {
            Block eggBlock = blockItem.getBlock();
            BlockState eggState = eggBlock.defaultBlockState();

            Optional<Property<?>> eggProperty =
                    eggBlock.defaultBlockState().getProperties().stream().filter(
                                    property ->
                                            property.getName().equals("eggs") && property instanceof IntegerProperty)
                            .findFirst();

            if (eggProperty.isPresent()) {
                eggState = eggBlock.defaultBlockState().setValue((IntegerProperty) eggProperty.get(), eggStack.getCount());
            }

            return Optional.of(eggState);
        }

        return Optional.empty();
    }

    public static NestBlockEntity getBlockEntity(Level level, BlockPos pos){
        return (NestBlockEntity) level.getBlockEntity(pos);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        var defaultBox= Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);

        if(level instanceof Level level1){

            if(getBlockEntity(level1, pos)==null || !state.getValue(HAS_EGG)){
                return defaultBox;
            }

            ItemStack eggStack= getBlockEntity(level1, pos).getEgg();

            if(getEggBlockState(eggStack).isPresent()){
                BlockState eggBlock= getEggBlockState(eggStack).get();
                VoxelShape eggShape= eggBlock.getShape(level, pos);
                AABB bounds = eggShape.bounds();

                var eggBox = Block.box(
                        bounds.minX * 16,
                        bounds.minY * 16 + 4,
                        bounds.minZ * 16,
                        bounds.maxX * 16,
                        bounds.maxY * 16 + 4,
                        bounds.maxZ * 16
                );

                defaultBox= Shapes.or(defaultBox, eggBox);
            }
        }

        return defaultBox;
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new NestBlockEntity(Primal_BlockEntities.NEST_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if(getBlockEntity(level, pos)!=null){
            serverTick(level, pos, state, getBlockEntity(level, pos));
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(HAS_EGG);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, NestBlockEntity blockEntity) {
        ItemStack eggStack= blockEntity.getEgg();

        if(!eggStack.isEmpty()){
            Optional<BlockState> eggState = NestBlock.getEggBlockState(eggStack);

            if(eggState.isPresent() && (eggState.get().is(Primal_Tags.Block.IS_ANIMAL_EGG) || Primal_Main.COMMON_CONFIG.extraPlaceableEggs.get().contains(BuiltInRegistries.BLOCK.getKey(eggState.get().getBlock()).toString()))){
                Optional<Property<?>> eggProperty=
                        eggState.get().getBlock().defaultBlockState().getProperties().stream().filter(
                                        property ->
                                                property.getName().equals("eggs") && property instanceof IntegerProperty)
                                .findFirst();

                randomEggTick(state, eggState.get(), level, pos, level.getRandom(), (IntegerProperty) eggProperty.orElse(null));
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected static void randomEggTick(@NotNull BlockState nestState, @NotNull BlockState eggState, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random, @Nullable IntegerProperty eggsProperty) {
        Map<String, String> eggMap = new HashMap<>();

        List<? extends List<? extends String>> eggDataList = Primal_Main.COMMON_CONFIG.eggData.get();

        for (List<? extends String> pair : eggDataList)
            if (pair.size() >= 2)
                eggMap.put(pair.get(0), pair.get(1)); // block_id -> entity_id

        ResourceLocation eggBlockId = BuiltInRegistries.BLOCK.getKey(eggState.getBlock());
        Optional<EntityType<?>> entityType = BuiltInRegistries.ENTITY_TYPE.getOptional(ResourceLocation.parse(eggMap.getOrDefault(eggBlockId.toString(), "")));

        if (level.random.nextInt(10) == 0 &&
                (eggState.getBlock() instanceof AnimalEgg || eggMap.containsKey(BuiltInRegistries.BLOCK.getKey(eggState.getBlock()).toString()))) {

            int i = nestState.getValue(NestBlock.HATCH);
            if (i < 2) {
                level.playSound(null, pos, Primal_Sounds.EGG_CRACK.get(), SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                level.setBlock(pos, nestState.setValue(NestBlock.HATCH, i + 1), 2);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(nestState));
            } else {
                //Hatch Sound
                level.playSound(null, pos, Primal_Sounds.EGG_HATCH.get(), SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);

                //Spawn entities
                if(eggsProperty!=null){
                    for (int j = 0; j < eggState.getValue(eggsProperty); j++) {
                        removeEgg(null, level, pos, nestState, false);
                        level.levelEvent(2001, pos, Block.getId(nestState));

                        //Primal eggs
                        if(eggState.getBlock() instanceof AnimalEgg animalEgg){
                            //Spawn primal animal
                            Animal animal = animalEgg.getAnimal().get().create(level);
                            if (animal != null) {
                                Holder<Biome> holder = level.getBiome(pos);
                                animal.setBaby(true);

                                if (animal instanceof VariantHolderPrimal variantWhenHatches && animal instanceof VariantHolder variantHolder) {

                                    var variantHolderCast =((VariantHolderPrimal<StringRepresentable, Animal>) variantWhenHatches);

                                    if(variantHolderCast.getRareVariant(animal)!=null && variantWhenHatches.getRareVariantProbability(level)){
                                        variantHolder.setVariant(variantHolderCast.getRareVariant(animal));
                                    } else {
                                        variantHolderCast.setVariantFromBiome(animal, holder);
                                    }
                                }


                                animal.moveTo((double)pos.getX() + 0.3 + (double)j * 0.2, pos.getY() +1, (double)pos.getZ() + 0.3, 0.0F, 0.0F);
                                level.addFreshEntity(animal);
                            }
                        }
                        //Other eggs
                        else if (entityType.isPresent()) {
                            Entity entity = entityType.get().create(level);

                            if(entity!=null){
                                if (entity instanceof AgeableMob ageable) {
                                    ageable.setBaby(true);
                                    if(entity instanceof Turtle turtle)
                                        turtle.setHomePos(pos);
                                }
                                entity.moveTo((double)pos.getX() + 0.3 + (double)j * 0.2, pos.getY() +1, (double)pos.getZ() + 0.3, 0.0F, 0.0F);
                                level.addFreshEntity(entity);
                            }
                        }
                    }
                }
                //Single eggs
                else if(entityType.isPresent()) {
                    removeEgg(null, level, pos, nestState, false);

                    Entity entity = entityType.get().create(level);

                    if (entity != null) {
                        if (entity instanceof AgeableMob ageable)
                            ageable.setBaby(true);
                        entity.moveTo(pos.getX(), pos.getY() +1, pos.getZ(), 0.0F, 0.0F);
                        level.addFreshEntity(entity);
                    }
                }

                NestBlockEntity nestBlockEntity = NestBlock.getBlockEntity(level, pos);
                BlockState emptyNestState =
                        nestState
                                .setValue(NestBlock.HAS_EGG, false)
                                .setValue(NestBlock.HATCH, 0);

                level.setBlock(pos, emptyNestState, 3);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(emptyNestState));
                nestBlockEntity.updateBlock();
            }
        }
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
