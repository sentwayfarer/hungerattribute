package net.lawliet.nea_hunger.clientServerSync;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.lawliet.nea_hunger.NeaHunger;
import org.jetbrains.annotations.NotNull;

public record PlayerSyncPacket(int hungerSprintValue) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PlayerSyncPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(NeaHunger.MODID, "effective_food_value"));

    public static final StreamCodec<ByteBuf, PlayerSyncPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            PlayerSyncPacket::hungerSprintValue,
            PlayerSyncPacket::new
    );


    @Override
    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
