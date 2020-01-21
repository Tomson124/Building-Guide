package de.tomson124.buildingguide.tileentity;

import de.tomson124.buildingguide.IBlockRotationMode;
import de.tomson124.buildingguide.blocks.BlockRotationMode;
import de.tomson124.buildingguide.blocks.OpenBlock;
import de.tomson124.buildingguide.geometry.LocalDirections;
import de.tomson124.buildingguide.geometry.Orientation;
import de.tomson124.buildingguide.network.*;
import de.tomson124.buildingguide.utils.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import openmods.api.IInventoryCallback;
import openmods.inventory.GenericInventory;
import openmods.network.rpc.RpcCallDispatcher;
import openmods.network.rpc.targets.TileEntityRpcTarget;
import openmods.reflection.TypeUtils;

public abstract class OpenTileEntity extends TileEntity implements IRpcTargetProvider {

	/** Place for TE specific setup. Called once upon creation */
	public void setup() {}

	public DimCoord getDimCoords() {
		return new DimCoord(world.provider.getDimension(), pos);
	}

	public Orientation getOrientation() {
		final IBlockState state = world.getBlockState(pos);
		return getOrientation(state);
	}

	public Orientation getOrientation(IBlockState state) {
		final Block block = state.getBlock();
		if (!(block instanceof OpenBlock)) return Orientation.XP_YP;
		final OpenBlock openBlock = (OpenBlock)block;
		return openBlock.getOrientation(state);
	}

	public IBlockRotationMode getRotationMode() {
		final IBlockState state = world.getBlockState(pos);
		return getRotationMode(state);
	}

	public IBlockRotationMode getRotationMode(IBlockState state) {
		final Block block = state.getBlock();
		if (!(block instanceof OpenBlock)) return BlockRotationMode.NONE;
		final OpenBlock openBlock = (OpenBlock)block;
		return openBlock.rotationMode;
	}

	public EnumFacing getFront() {
		final IBlockState state = world.getBlockState(pos);
		return getFront(state);
	}

	public EnumFacing getFront(IBlockState state) {
		final Block block = state.getBlock();
		if (!(block instanceof OpenBlock)) return EnumFacing.NORTH;
		final OpenBlock openBlock = (OpenBlock)block;
		return openBlock.getFront(state);
	}

	public EnumFacing getBack() {
		return getFront().getOpposite();
	}

	public LocalDirections getLocalDirections() {
		final IBlockState state = world.getBlockState(pos);
		final Block block = state.getBlock();
		if (!(block instanceof OpenBlock)) return LocalDirections.fromFrontAndTop(EnumFacing.NORTH, EnumFacing.UP);
		final OpenBlock openBlock = (OpenBlock)block;
		return openBlock.getLocalDirections(state);
	}

	public boolean isAddedToWorld() {
		return world != null;
	}

	protected TileEntity getTileEntity(BlockPos blockPos) {
		return (world != null && world.isBlockLoaded(blockPos))? world.getTileEntity(blockPos) : null;
	}

	public TileEntity getTileInDirection(EnumFacing direction) {
		return getTileEntity(pos.offset(direction));
	}

	public boolean isAirBlock(EnumFacing direction) {
		return world != null && world.isAirBlock(getPos().offset(direction));
	}

	protected void playSoundAtBlock(SoundEvent sound, SoundCategory category, float volume, float pitch) {
		BlockUtils.playSoundAtPos(world, pos, sound, category, volume, pitch);
	}

	protected void playSoundAtBlock(SoundEvent sound, float volume, float pitch) {
		playSoundAtBlock(sound, SoundCategory.BLOCKS, volume, pitch);
	}

	protected void spawnParticle(EnumParticleTypes particle, double dx, double dy, double dz, double vx, double vy, double vz, int... args) {
		world.spawnParticle(particle, pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz, vx, vy, vz, args);
	}

	protected void spawnParticle(EnumParticleTypes particle, double vx, double vy, double vz, int... args) {
		spawnParticle(particle, 0.5, 0.5, 0.5, vx, vy, vz, args);
	}

	public void sendBlockEvent(int event, int param) {
		world.addBlockEvent(pos, getBlockType(), event, param);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	public void openGui(Object instance, EntityPlayer player) {
		player.openGui(instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
	}

	public AxisAlignedBB getBB() {
		return new AxisAlignedBB(pos, pos.add(1, 1, 1));
	}

	@Override
	public IRpcTarget createRpcTarget() {
		return new TileEntityRpcTarget(this);
	}

	public <T> T createProxy(final IPacketSender sender, Class<? extends T> mainIntf, Class<?>... extraIntf) {
		TypeUtils.isInstance(this, mainIntf, extraIntf);
		return RpcCallDispatcher.instance().createProxy(createRpcTarget(), sender, mainIntf, extraIntf);
	}

	public <T> T createClientRpcProxy(Class<? extends T> mainIntf, Class<?>... extraIntf) {
		final IPacketSender sender = RpcCallDispatcher.instance().senders.client;
		return createProxy(sender, mainIntf, extraIntf);
	}

	public <T> T createServerRpcProxy(Class<? extends T> mainIntf, Class<?>... extraIntf) {
		final IPacketSender sender = RpcCallDispatcher.instance().senders.block.bind(getDimCoords());
		return createProxy(sender, mainIntf, extraIntf);
	}

	public void markUpdated() {
		world.markChunkDirty(pos, this);
	}

	protected IInventoryCallback createInventoryCallback() {
		return (inventory, slotNumber) -> markUpdated();
	}

	protected GenericInventory registerInventoryCallback(GenericInventory inventory) {
		return inventory.addCallback(createInventoryCallback());
	}

	public boolean isValid(EntityPlayer player) {
		return (world.getTileEntity(pos) == this) && (player.getDistanceSqToCenter(pos) <= 64.0D);
	}
}