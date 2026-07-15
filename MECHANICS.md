# ATXCore Mechanics Catalog

All built-in mechanic IDs are plain IDs. For example, use `message`, not `atxcore:message`.

Future plugins can still register namespaced IDs if they want, such as `myplugin:custom_effect`.

Recommended future-plugin YAML shape:

```yaml
chains:
  bleed-hit:
    trigger: "melee_attack"
    conditions:
      - id: "actor_health_above"
        data:
          amount: 20
    mutators:
      - id: "target_victim"
    filters:
      - id: "not_target_entity_type"
        data:
          type: "minecraft:creeper"
    effects:
      - id: "damage"
        data:
          amount: 4
```

Each step's `data` is local config for that mechanic. Trigger/event runtime data is separate and can be read through `{data.key}`.

## Effects

- `message`
- `console_command`
- `sound`
- `broadcast`
- `action_bar`
- `title`
- `clear_title`
- `player_command`
- `op_command`
- `teleport`
- `teleport_world_spawn`
- `heal`
- `add_health`
- `damage`
- `kill`
- `feed`
- `add_food`
- `set_saturation`
- `extinguish`
- `set_fire_ticks`
- `add_potion`
- `remove_potion`
- `clear_potions`
- `give_item`
- `take_item`
- `clear_inventory`
- `set_gamemode`
- `set_flying`
- `set_allow_flight`
- `set_exp`
- `give_exp`
- `set_level`
- `add_level`
- `spawn_particle`
- `strike_lightning_effect`
- `strike_lightning`
- `explosion`
- `set_time`
- `set_weather_clear`
- `set_weather_rain`
- `set_weather_thunder`
- `play_world_sound`
- `stop_sound`
- `set_velocity`
- `launch_up`
- `close_inventory`
- `open_workbench`
- `kick`
- `set_display_name`
- `reset_display_name`
- `set_player_list_name`
- `set_glowing`
- `set_invulnerable`
- `spawn_entity`
- `delay_console_command`
- `delayed_message`
- `repeat_console_command`
- `set_max_health`
- `add_max_health`
- `reset_max_health`
- `set_attack_damage`
- `add_attack_damage`
- `set_attack_speed`
- `set_movement_speed`
- `add_movement_speed`
- `set_armor`
- `add_armor`
- `set_armor_toughness`
- `set_knockback_resistance`
- `set_luck`
- `set_follow_range`
- `set_flying_speed_attribute`
- `set_attack_knockback`
- `set_max_absorption_attribute`
- `set_safe_fall_distance`
- `set_scale`
- `set_step_height`
- `set_gravity_attribute`
- `set_jump_strength`
- `set_block_interaction_range`
- `set_entity_interaction_range`
- `set_block_break_speed`
- `set_mining_efficiency`
- `set_sneaking_speed`
- `set_submerged_mining_speed`
- `set_sweeping_damage_ratio`
- `set_attribute`
- `add_attribute`
- `reset_attribute`
- `set_absorption`
- `add_absorption`
- `set_exhaustion`
- `add_exhaustion`
- `set_remaining_air`
- `set_freeze_ticks`
- `set_no_damage_ticks`
- `increment_stat`
- `decrement_stat`
- `set_stat`
- `reset_stat`
- `vault_deposit`
- `vault_withdraw`
- `vault_set_balance`

## Triggers

- `player_join`
- `player_quit`
- `player_kick`
- `player_death`
- `player_respawn`
- `player_changed_world`
- `player_move`
- `player_teleport`
- `player_chat`
- `player_command`
- `player_interact`
- `player_drop_item`
- `player_pickup_item`
- `player_consume_item`
- `player_item_break`
- `player_swap_hands`
- `player_level_change`
- `player_exp_change`
- `block_break`
- `block_place`
- `entity_damage`
- `entity_damage_by_entity`
- `entity_death`
- `food_level_change`
- `projectile_hit`
- `entity_shoot_bow`
- `inventory_click`
- `inventory_open`
- `inventory_close`
- `player_fish`
- `melee_attack`
- `projectile_attack`

Trigger-provided context data includes the `event` key and any relevant event values, such as `block_type`, `material`, `amount`, `damage`, `final_damage`, `cause`, `slot`, `click`, `state`, `old_level`, or `new_level`.

## Conditions

- `permission`
- `chance`
- `placeholder_equals`
- `player_online`
- `actor_player`
- `target_player`
- `actor_health_above`
- `target_health_above`
- `actor_has_permission`
- `world`
- `gamemode`
- `health_above`
- `health_below`
- `food_above`
- `food_below`
- `level_at_least`
- `level_below`
- `has_item`
- `holding_item`
- `sneaking`
- `sprinting`
- `flying`
- `op`
- `has_potion`
- `in_region_box`
- `data_exists`
- `data_equals`
- `string_contains`
- `number_greater`
- `number_less`
- `biome`
- `dimension`
- `weather`
- `time_between`
- `attribute_at_least`
- `attribute_below`
- `max_health_at_least`
- `attack_damage_at_least`
- `movement_speed_at_least`
- `stat_at_least`
- `stat_below`
- `vault_has_money`
- `vault_balance_at_least`

## Filters

`target_*` filters inspect the trigger's original target. `actor_*` filters inspect the original actor. Generic filters such as `entity_only`, `health_range`, and `attribute_range` inspect the current active context after mutators run.

- `player_only`
- `entity_only`
- `location_only`
- `target_player_only`
- `actor_player_only`
- `target_entity_type`
- `not_target_entity_type`
- `actor_entity_type`
- `not_actor_entity_type`
- `target_not_creeper`
- `world`
- `not_world`
- `permission`
- `no_permission`
- `gamemode`
- `survival`
- `creative`
- `has_item`
- `holding_item`
- `health_range`
- `level_range`
- `biome`
- `dimension`
- `data_exists`
- `data_equals`
- `random_percent`
- `name_contains`
- `attribute_range`
- `vault_has_money`

## Mutators

- `target_actor`
- `target_attacker`
- `target_self`
- `target_victim`
- `target_target`
- `target_trigger_location`
- `actor_to_data`
- `target_to_data`
- `set_data`
- `remove_data`
- `clear_data`
- `copy_data`
- `append_data`
- `prepend_data`
- `uppercase_data`
- `lowercase_data`
- `increment_data`
- `multiply_data`
- `clamp_data`
- `set_message`
- `set_command`
- `set_amount`
- `set_world`
- `set_coordinates`
- `data_if_absent`
- `toggle_boolean_data`
- `random_int_data`
- `random_decimal_data`
- `round_data`
- `player_to_data`
- `location_to_data`
- `attribute_to_data`
- `stat_to_data`
- `vault_balance_to_data`

## Placeholders

- `{player}`
- `{player_uuid}`
- `{player_display}`
- `{player_list_name}`
- `{player_health}`
- `{player_max_health}`
- `{player_food}`
- `{player_saturation}`
- `{player_level}`
- `{player_exp}`
- `{player_gamemode}`
- `{player_ping}`
- `{player_locale}`
- `{player_world}`
- `{player_x}`
- `{player_y}`
- `{player_z}`
- `{player_biome}`
- `{player_flying}`
- `{player_sneaking}`
- `{player_sprinting}`
- `{entity_type}`
- `{entity_uuid}`
- `{entity_world}`
- `{world}`
- `{world_time}`
- `{world_full_time}`
- `{world_environment}`
- `{world_weather}`
- `{world_players}`
- `{x}`
- `{y}`
- `{z}`
- `{yaw}`
- `{pitch}`
- `{block_type}`
- `{biome}`
- `{server_online}`
- `{server_max}`
- `{server_name}`
- `{server_version}`
- `{server_motd}`
- `{tps_1m}`
- `{tps_5m}`
- `{tps_15m}`
- `{vault_balance}`
- `{vault_balance_formatted}`
- `{vault_currency_singular}`
- `{vault_currency_plural}`
- `{data.key_name}`

PlaceholderAPI exposes the same providers as `%atxcore_<placeholder>%`, for example `%atxcore_player%`.
