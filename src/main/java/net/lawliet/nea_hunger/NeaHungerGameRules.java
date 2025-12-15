package net.lawliet.nea_hunger;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import net.lawliet.nea_hunger.clientServerSync.PlayerSyncPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;


//@EventBusSubscriber(modid = NeaHunger.MODID)
public class NeaHungerGameRules {
//    public static GameRule<Integer> RULE_HUNGER_HEALING_PERCENTAGE;
//    public static GameRule<Integer> RULE_HUNGER_SPRINT_VALUE;

    public static final DeferredRegister<@NotNull GameRule<?>> GAMERULES = DeferredRegister.create(Registries.GAME_RULE, NeaHunger.MODID);

    public static final Supplier<GameRule<@NotNull Integer>> RULE_HUNGER_HEALING_PERCENTAGE = GAMERULES.register("hunger_healing_percentage", () ->  registerIntegerGameRule(GameRuleCategory.PLAYER, 90, 0, 100, FeatureFlagSet.of())
            );
    public static final Supplier<GameRule<@NotNull Integer>> RULE_HUNGER_SPRINT_VALUE = GAMERULES.register("hunger_sprint_value", () -> registerIntegerGameRule(GameRuleCategory.PLAYER, 6, 0, Integer.MAX_VALUE, FeatureFlagSet.of(), value -> value));


    // Helpers
    @SuppressWarnings("unused")
    private static GameRule<@NotNull Integer> registerIntegerGameRule(GameRuleCategory gameRuleCategory, int defaultValue, int minValue) {
        return registerIntegerGameRule(gameRuleCategory, defaultValue, minValue, Integer.MAX_VALUE, FeatureFlagSet.of());
    }
    private static GameRule<@NotNull Integer> registerIntegerGameRule(GameRuleCategory gameRuleCategory, int defaultValue, int minValue, int maxValue, FeatureFlagSet featureFlagSet) {
        return  registerIntegerGameRule(gameRuleCategory, defaultValue, minValue, maxValue, featureFlagSet, value -> value);
    }

    private static GameRule<@NotNull Integer> registerIntegerGameRule(GameRuleCategory gameRuleCategory, int defaultValue, int minValue, int maxValue, FeatureFlagSet featureFlagSet, ToIntFunction<Integer> runFunction) {
        return new GameRule<>(
                gameRuleCategory,
                GameRuleType.INT,
                IntegerArgumentType.integer(minValue, maxValue),
                GameRuleTypeVisitor::visitInteger,
                Codec.intRange(minValue, maxValue),
                runFunction,
                defaultValue,
                featureFlagSet
        );
    }


    public static void register(IEventBus event) {
        GAMERULES.register(event);
    }

    public static <T> void GameRuleChangeEvent(GameRule<@NotNull T> gameRule, T value, MinecraftServer minecraftServer) {
        if (gameRule == RULE_HUNGER_SPRINT_VALUE.get()) {
            for(ServerPlayer serverPlayer : minecraftServer.getPlayerList().getPlayers()) {
                PacketDistributor.sendToPlayer(serverPlayer,new PlayerSyncPacket((int) value));
            }
        }
    }

//    @SubscribeEvent
//    public static void commonSetup(FMLCommonSetupEvent event) {
//     RULE_HUNGER_HEALING_PERCENTAGE = GameRules.register("hungerHealingPercentage", GameRuleCategory.PLAYER, GameRules.IntegerValue.create(90));
//     RULE_HUNGER_SPRINT_VALUE = GameRules.register("hungerSprintValue",GameRuleCategory.PLAYER,GameRules.IntegerValue.create(6, (player,value) -> {
//         for(ServerPlayer serverPlayer : player.getPlayerList().getPlayers()) {
//             PacketDistributor.sendToPlayer(serverPlayer,new PlayerSyncPacket(value.get()));
//         }
//     }));
//    }

}
