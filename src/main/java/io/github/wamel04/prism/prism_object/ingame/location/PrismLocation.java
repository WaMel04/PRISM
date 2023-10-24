package io.github.wamel04.prism.prism_object.ingame.location;

import io.github.wamel04.prism.prism_object.ingame.world.PrismWorld;

public class PrismLocation {

    PrismWorld prismWorld;
    double x;
    double y;
    double z;
    float pitch = 0.0f;
    float yaw = 0.0f;

    public PrismLocation(PrismWorld prismWorld, double x, double y, double z) {
        this.prismWorld = prismWorld;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PrismLocation(PrismWorld prismWorld, double x, double y, double z, float pitch, float yaw) {
        this.prismWorld = prismWorld;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public PrismWorld getPrismWorld() {
        return prismWorld;
    }

    public void setPrismWorld(PrismWorld prismWorld) {
        this.prismWorld = prismWorld;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }


}
