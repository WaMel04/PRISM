package io.github.wamel04.prism.example;

import io.github.wamel04.prism.prismobject.PrismObject;

public class House implements PrismObject {

    String ownerUuid;
    CustomLocation customLocation;

    public House(String ownerUuid, CustomLocation customLocation) {
        this.ownerUuid = ownerUuid;
        this.customLocation = customLocation;
    }

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public CustomLocation getCustomLocation() {
        return customLocation;
    }

    public void setCustomLocation(CustomLocation customLocation) {
        this.customLocation = customLocation;
    }

    @Override
    public String getNameKey() {
        return "mcu:server:house";
    }
}
