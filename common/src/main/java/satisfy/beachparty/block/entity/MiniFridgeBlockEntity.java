package satisfy.beachparty.block.entity;

import de.cristelknight.doapi.common.world.ImplementedInventory;
import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import satisfy.beachparty.client.gui.handler.MiniFridgeGuiHandler;
import satisfy.beachparty.recipe.MiniFridgeRecipe;
import satisfy.beachparty.registry.BlockEntityRegistry;
import satisfy.beachparty.registry.RecipeRegistry;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class MiniFridgeBlockEntity extends BlockEntity implements ImplementedInventory, BlockEntityTicker<MiniFridgeBlockEntity>, MenuProvider {
    private static final int[] SLOTS_FOR_SIDE = new int[]{2};
    private static final int[] SLOTS_FOR_UP = new int[]{1};
    private static final int[] SLOTS_FOR_DOWN = new int[]{0};

    private NonNullList<ItemStack> inventory;
    public static final int CAPACITY = 3;
    private static final int OUTPUT_SLOT = 0;
    private int fermentationTime = 0;
    private int totalFermentationTime;
    protected float experience;

    private final ContainerData propertyDelegate = new ContainerData() {

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> MiniFridgeBlockEntity.this.fermentationTime;
                case 1 -> MiniFridgeBlockEntity.this.totalFermentationTime;
                default -> 0;
            };
        }


        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> MiniFridgeBlockEntity.this.fermentationTime = value;
                case 1 -> MiniFridgeBlockEntity.this.totalFermentationTime = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public MiniFridgeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.MINI_FRIDGE_BLOCK_ENTITY.get(), pos, state);
        this.inventory = NonNullList.withSize(CAPACITY, ItemStack.EMPTY);
    }


    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, this.inventory, provider);
        this.fermentationTime = compoundTag.getShort("FermentationTime");
        this.experience = compoundTag.getFloat("Experience");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        ContainerHelper.saveAllItems(compoundTag, this.inventory, provider);
        compoundTag.putFloat("Experience", this.experience);
        compoundTag.putShort("FermentationTime", (short) this.fermentationTime);
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state, MiniFridgeBlockEntity blockEntity) {
        if (world.isClientSide) return;
        AtomicBoolean dirty = new AtomicBoolean(false);

        RecipeManager recipeManager = world.getRecipeManager();
        List<RecipeHolder<MiniFridgeRecipe>> recipes = recipeManager.getAllRecipesFor(RecipeRegistry.MINI_FRIDGE_RECIPE_TYPE.get());
        Optional<MiniFridgeRecipe> recipeOptional = Optional.ofNullable(getRecipe(recipes, inventory));

        recipeOptional.ifPresent(recipe -> {
            RegistryAccess access = world.registryAccess();
            if (canCraft(recipe, access)) {
                this.fermentationTime++;
                if (this.fermentationTime >= this.totalFermentationTime) {
                    this.fermentationTime = 0;
                    craft(recipe, access);
                    dirty.set(true);
                }
            } else {
                this.fermentationTime = 0;
            }
        });

        if (dirty.get()) {
            setChanged();
        }

    }

    private MiniFridgeRecipe getRecipe(List<RecipeHolder<MiniFridgeRecipe>> recipes, NonNullList<ItemStack> inventory) {
        recipeLoop:
        for (RecipeHolder<MiniFridgeRecipe> recipeHolder : recipes) {
            MiniFridgeRecipe recipe = recipeHolder.value();
            for (Ingredient ingredient : recipe.getIngredients()) {
                boolean ingredientFound = false;
                for (int slotIndex = 1; slotIndex < inventory.size(); slotIndex++) {
                    ItemStack slotItem = inventory.get(slotIndex);
                    if (ingredient.test(slotItem)) {
                        ingredientFound = true;
                        break;
                    }
                }
                if (!ingredientFound) {
                    continue recipeLoop;
                }
            }
            return recipe;
        }
        return null;
    }

    private boolean canCraft(MiniFridgeRecipe recipe, RegistryAccess access) {
        if (recipe == null || recipe.getResultItem(access).isEmpty()) {
            return false;
        } else if (areInputsEmpty()) {
            return false;
        }
        ItemStack itemStack = this.getItem(OUTPUT_SLOT);
        return itemStack.isEmpty() || itemStack == recipe.getResultItem(access);
    }


    private boolean areInputsEmpty() {
        int emptyStacks = 0;
        for (int i = 1; i <= 2; i++) {
            if (this.getItem(i).isEmpty()) emptyStacks++;
        }
        return emptyStacks == 2;
    }

    private void craft(MiniFridgeRecipe recipe, RegistryAccess access) {
        if (!canCraft(recipe, access)) {
            return;
        }
        final ItemStack recipeOutput = recipe.getResultItem(access);
        final ItemStack outputSlotStack = this.getItem(OUTPUT_SLOT);
        if (outputSlotStack.isEmpty()) {
            ItemStack output = recipeOutput.copy();
            setItem(OUTPUT_SLOT, output);
        }
        for (int slot = 1; slot <= 2; slot++) {
            ItemStack slotStack = this.getItem(slot);
            for (Ingredient ingredient : recipe.getIngredients()) {
                if (ingredient.test(slotStack)) {
                    slotStack.shrink(1);
                    if (!slotStack.isEmpty()) {
                        setItem(slot, slotStack);
                    } else {
                        this.setItem(slot, ItemStack.EMPTY);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction side) {
        if(side.equals(Direction.UP)){
            return SLOTS_FOR_UP;
        } else if (side.equals(Direction.DOWN)){
            return SLOTS_FOR_DOWN;
        } else return SLOTS_FOR_SIDE;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        final ItemStack stackInSlot = this.inventory.get(slot);
        boolean dirty = !stack.isEmpty() && ItemStack.isSameItem(stack, stackInSlot) && ItemStack.matches(stack, stackInSlot);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        if (slot == 1 || slot == 2) {
            if (!dirty) {
                this.totalFermentationTime = 50;
                this.fermentationTime = 0;
                setChanged();
            }
        }
    }
    @Override
    public boolean stillValid(Player player) {
        assert this.level != null;
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 0.5, (double)this.worldPosition.getZ() + 0.5) <= 64.0;
        }
    }


    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        return new MiniFridgeGuiHandler(syncId, inv, this, this.propertyDelegate);
    }
}