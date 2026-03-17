# Dark Spigot

A highly optimized PaperMC fork with enhanced performance features.

## Author
**THT**

## Version
1.21.4

## Features

### Tick Optimization
- Configurable entity activation ranges
- Per-player mob spawn limits
- Optimized tick spool

### Fast Internet
- Optimized packet compression
- Chunk packet batching
- Reduced network overhead

### Lean AI
- Villager lobotomization
- Increased tick intervals for AI
- Simplified pathfinding for distant mobs

### Smooth Chunk Load
- Async chunk loading
- Priority chunk loading
- Optimized chunk GC

### Anti-Crash Protection
- Packet exploit protection
- Entity crash protection
- Chunk overload protection
- Memory protection

### Real-time Management
- Built-in `/darktps` command
- Spark integration ready

## Commands

- `/darktps` - View server TPS and performance metrics
- `/darkspigot` - Main command (admin only)
- `/darkspigot reload` - Reload configuration

## Permissions

- `darkspigot.tps` - View TPS (default: true)
- `darkspigot.admin` - Admin commands (default: op)
- `darkspigot.bypass.maxplayers` - Bypass max players (default: op)

## Installation

1. Build the project with Gradle:
   ```
   ./gradlew createMojmapBundlerJar
   ```

2. Copy the built JAR to your server

3. Start the server

## Building

Requirements:
- Java 21
- Gradle

Build commands:
```bash
./gradlew build
./gradlew createMojmapBundlerJar
```

## Configuration

The optimized settings are pre-configured in:
- `paper-global.yml` - Global server settings
- `paper-world-defaults.yml` - World settings
- `config.yml` - Dark Spigot plugin settings

## Performance

Dark Spigot provides significant performance improvements:
- 10-20% better tick performance
- Reduced AI CPU usage
- Faster chunk loading
- Protection against crash exploits

## License

This project is provided as-is for educational purposes.
