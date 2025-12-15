package net.lawliet.nea_hunger.events;

import net.lawliet.nea_hunger.NeaHunger;
import net.lawliet.nea_hunger.NeaHungerAttributes;
import net.lawliet.nea_hunger.NeaHungerGameRules;
import net.lawliet.nea_hunger.clientServerSync.ClientPacketListner;
import net.lawliet.nea_hunger.clientServerSync.PlayerSyncPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


@EventBusSubscriber(modid = NeaHunger.MODID)
public class GameEvents {
    @SubscribeEvent
    public static void syncGameRulesPackets(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            Level level = event.getEntity().level();
            if (level instanceof ServerLevel serverLevel) {
                int hungerSprintValue = serverLevel.getGameRules().get(NeaHungerGameRules.RULE_HUNGER_SPRINT_VALUE.get());
                PacketDistributor.sendToPlayer((ServerPlayer) event.getEntity(), new PlayerSyncPacket(hungerSprintValue));
            }

        }
    }

    @SubscribeEvent
    public static void addAttribute(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, NeaHungerAttributes.MAX_HUNGER,20.0D);
        event.add(EntityType.PLAYER, NeaHungerAttributes.MAX_SATURATION,20.0D);
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.NETWORK);
        registrar.playToClient(
                PlayerSyncPacket.TYPE,
                PlayerSyncPacket.STREAM_CODEC,
                ClientPacketListner::handleDataOnNetwork
        );
    }
}
