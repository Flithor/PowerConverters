package powercrystals.powerconverters.power.conduit;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import powercrystals.powerconverters.gui.PCCreativeTab;
import powercrystals.powerconverters.util.IUpdateTileWithCords;

public class BlockEnergyConduit extends BlockContainer {

	public BlockEnergyConduit() {
		super(Material.rock);
		setBlockName("powerconverters.conduit");
		setCreativeTab(PCCreativeTab.tab);
	}

	@Override
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
		TileEntity te = world instanceof World ? world.getTileEntity(x, y, z) : world.getTileEntity(x, y, z);
		if (te instanceof IUpdateTileWithCords) {
			((IUpdateTileWithCords) te).onNeighboorChanged(tileX, tileY, tileZ);
		}
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par1) {
		return new TileEnergyConduit();
	}
}