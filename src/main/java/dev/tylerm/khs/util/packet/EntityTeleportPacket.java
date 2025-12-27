package dev.tylerm.khs.util.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.InternalStructure;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class EntityTeleportPacket extends AbstractPacket {

    public EntityTeleportPacket() {
        super(PacketType.Play.Server.ENTITY_TELEPORT);
    }

    public void setEntity(@NotNull Entity entity) {
        super.packet.getIntegers().write(0, entity.getEntityId());
    }

    public void setPosition(Location location) {
        InternalStructure is = super.packet.getStructures().getValues().get(0);

        is.getVectors()
                .write(0, new Vector(location.getX(), location.getY(), location.getZ()))
                .write(1, new Vector(0, 0, 0));

        is.getFloat().write(0, location.getYaw());
    }

    public void setX(double x) {
        super.packet.getDoubles().write(0, x);
    }

    public void setY(double y) {
        super.packet.getDoubles().write(1, y);
    }

    public void setZ(double z) {
        super.packet.getDoubles().write(2, z);
    }
}
