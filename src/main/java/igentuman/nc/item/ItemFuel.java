package igentuman.nc.item;
import igentuman.nc.content.fuel.FuelDef;
import igentuman.nc.content.fuel.FuelManager;
import igentuman.nc.util.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import static igentuman.nc.handler.event.client.InputEvents.DESCRIPTIONS_SHOW;

public class ItemFuel extends Item {

    public FuelDef def;
    public double heat = 0;
    public int forge_energy = 0;
    public double heat_boiling = 0;
    public int criticality = 0;
    public int depletion = 0;
    public int efficiency = 0;
    public final String group;
    public final String name;
    public final String subType;

    private boolean initialized = false;

    public ItemFuel(Properties pProperties, String group, String name, String subType) {
        super(pProperties);
        this.group = group;
        this.name = name;
        this.subType = subType;
    }

    public ItemFuel initDefinition()
    {
        if(initialized) return this;
        def = FuelManager.all().get(group).get(name).subType(subType);
        heat = def.getHeatFEMode();
        heat_boiling = def.getHeatBoilingMode();
        criticality = def.criticality;
        depletion = def.depletion;
        efficiency = def.efficiency;
        forge_energy = def.forge_energy;
        return this;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag)
    {
        initDefinition();
        if(!DESCRIPTIONS_SHOW) {
            list.add(TextUtils.applyFormat(Component.translatable("tooltip.toggle_description_keys"), ChatFormatting.GRAY));
        } else {
            list.add(TextUtils.applyFormat(Component.translatable("fuel.heat.descr", TextUtils.numberFormat(heat)), ChatFormatting.GOLD));
            //list.add(TextUtils.applyFormat(Component.translatable("fuel.heat_boiling.descr", TextUtils.numberFormat(heat_boiling)), ChatFormatting.YELLOW));
            list.add(TextUtils.applyFormat(Component.translatable("fuel.forge_energy.descr", forge_energy), ChatFormatting.BLUE));
            //list.add(TextUtils.applyFormat(Component.translatable("fuel.criticality.descr", criticality), ChatFormatting.RED));
            list.add(TextUtils.applyFormat(Component.translatable("fuel.depletion.descr", depletion), ChatFormatting.GREEN));
            //list.add(TextUtils.applyFormat(Component.translatable("fuel.efficiency.descr", efficiency), ChatFormatting.DARK_PURPLE));
            list.add(TextUtils.applyFormat(Component.translatable("fuel.description"), ChatFormatting.AQUA));
        }
    }
}
