package io.github.debuggyteam.tablesaw;

import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.resource.ResourceType;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.quiltmc.qsl.networking.api.ServerPlayConnectionEvents;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourceLoaderEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableSaw implements ModInitializer {
    public static final String MODID = "tablesaw";
    public static final Identifier TABLESAW_CHANNEL = identifier("tablesaw");  // S <-> C
    public static final Identifier RECIPE_CHANNEL = identifier("recipe_sync"); // S -> C only
    public static final int MESSAGE_ENGAGE_TABLESAW = 0x120000;

    public static final Logger LOGGER = LoggerFactory.getLogger("TableSaw");

    public static final TableSawBlock TABLESAW = new TableSawBlock(QuiltBlockSettings.of(Material.WOOD).nonOpaque());
    public static final ScreenHandlerType<TableSawScreenHandler> TABLESAW_SCREEN_HANDLER = new ScreenHandlerType<>((syncId, inventory) -> new TableSawScreenHandler(syncId, inventory, ScreenHandlerContext.EMPTY));

    /** Creates an identifier with this mod as the namespace */
    public static Identifier identifier(String path) {
    	return new Identifier(MODID, path);
    }
    
    @Override
    public void onInitialize(ModContainer mod) {
        LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());

        Registry.register(Registry.SCREEN_HANDLER, new Identifier(MODID, "tablesaw"), TABLESAW_SCREEN_HANDLER);
        
        Registry.register(Registry.BLOCK, new Identifier(MODID, "tablesaw"), TABLESAW);
        Registry.register(Registry.ITEM, new Identifier(MODID, "tablesaw"),
                new BlockItem(TABLESAW, new QuiltItemSettings().group(ItemGroup.DECORATIONS)));
        
        // Receives serverside notice that the tablesaw craft button is clicked
        ServerPlayNetworking.registerGlobalReceiver(TABLESAW_CHANNEL, new TableSawServerReceiver());
        
        // Updates recipes on the server when data is loaded or reloaded
        ResourceLoader.get(ResourceType.SERVER_DATA).registerReloader(new TableSawResourceLoader());
        
        // Syncs recipes for all connected players when a live reload happens
        ResourceLoaderEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, error) -> {
            // Server will be null here if this is the first reload as the game is starting. In that case there are no players to notify.
            if (server == null) return;
            for(ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                TableSawRecipeSync.syncFromServer(server, player);
            }
        });
        
        // Syncs recipes when a player connects
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            TableSawRecipeSync.syncFromServer(server, handler.getPlayer());
        });
    }
    
    
}
