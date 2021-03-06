package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IShearable;

import java.util.List;
import java.util.Random;

public class BlockTFTrollRoot extends Block implements IShearable {

	protected static final VoxelShape AABB = VoxelShapes.create(new AxisAlignedBB(0.1, 0.0, 0.1, 0.9, 1.0, 0.9));

	protected BlockTFTrollRoot() {
		super(Properties.create(Material.PLANTS).sound(SoundType.PLANT).tickRandomly().doesNotBlockMovement());
		//this.setCreativeTab(TFItems.creativeTab); TODO 1.14
	}

	@Override
	public boolean isShearable(ItemStack item, IWorldReader world, BlockPos pos) {
		return true;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IWorld world, BlockPos pos, int fortune) {
		NonNullList<ItemStack> ret = NonNullList.create();
		ret.add(new ItemStack(this));
		return ret;
	}

	private boolean canBlockStay(IWorldReader world, BlockPos pos) {
		return canPlaceRootBelow(world, pos.up());
	}

	public static boolean canPlaceRootBelow(IWorldReader world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		Block blockAbove = state.getBlock();

		return state.getMaterial() == Material.ROCK || blockAbove == TFBlocks.trollvidr.get() || blockAbove == TFBlocks.trollber.get() || blockAbove == TFBlocks.unripe_trollber.get();
	}

	@Override
	@Deprecated
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return super.isValidPosition(state, world, pos) && this.canBlockStay(world, pos);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return AABB;
	}

	//TODO: Check this
//	@Override
//	public boolean isSolid(BlockState state) {
//		return false;
//	}

	@Override
	@Deprecated
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.checkAndDropBlock(world, pos);
	}

	@Override
	@Deprecated
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		this.checkAndDropBlock(world, pos);
	}

	private void checkAndDropBlock(World world, BlockPos pos) {
		if (!this.canBlockStay(world, pos)) {
			world.destroyBlock(pos, true);
		}
	}

//	@Override
//	public int quantityDropped(BlockState state, int fortune, Random random) {
//		return 0;
//	}

	//TODO: Move to client
//	@OnlyIn(Dist.CLIENT)
//	@Override
//	public BlockRenderLayer getRenderLayer() {
//		return BlockRenderLayer.CUTOUT;
//	}
}
