package powercrystals.powerconverters.power.te;

import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;
import powercrystals.powerconverters.PowerConverterCore;
import powercrystals.powerconverters.common.TileEntityEnergyBridge;
import powercrystals.powerconverters.init.PowerSystems;
import powercrystals.powerconverters.power.TileEntityEnergyProducer;
import powercrystals.powerconverters.temp.BlockPosition;

public class TileEntityThermalExpansionProducer extends TileEntityEnergyProducer<IEnergyHandler> implements IEnergyHandler {

	public TileEntityThermalExpansionProducer() {
		super(PowerSystems.powerSystemThermalExpansion, 0, IEnergyHandler.class);
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection arg0) {
		return true;
	}

	@Override
	public int extractEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
		return 0;
	}

	@Override
	public int getEnergyStored(ForgeDirection arg0) {
		TileEntityEnergyBridge bridge = getFirstBridge();
		if (bridge == null)
			return 0;
		return (int) (bridge.getEnergyStored() / getPowerSystem().getInternalEnergyPerInput());
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection arg0) {
		TileEntityEnergyBridge bridge = getFirstBridge();
		if (bridge == null)
			return 0;
		return (int) (bridge.getEnergyStoredMax() / getPowerSystem().getInternalEnergyPerInput());
	}

	@Override
	public int receiveEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
		return 0;
	}

	@Override
	public double produceEnergy(double energy) {
		final double toUseRF = energy / getPowerSystem().getInternalEnergyPerOutput();

		if (toUseRF > 0) {
			List<BlockPosition> pos = new BlockPosition(xCoord, yCoord, zCoord).getAdjacent(true);
			for (BlockPosition p : pos) {
				TileEntity te = worldObj.getTileEntity(p.x, p.y, p.z);
				if ((te instanceof IEnergyHandler) && !((te instanceof TileEntityThermalExpansionConsumer) || (te instanceof TileEntityEnergyBridge))) {
					IEnergyHandler handler = (IEnergyHandler) te;
					final double RF = handler.receiveEnergy(p.orientation, (int) (toUseRF), false);
					energy -= RF * getPowerSystem().getInternalEnergyPerOutput();
					if (energy <= 0)
						break;
				}
			}

		}
		return energy;
	}

}