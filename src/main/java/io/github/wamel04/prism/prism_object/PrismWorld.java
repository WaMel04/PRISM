package io.github.wamel04.prism.prism_object;

public class PrismWorld {

    PrismServer prismServer;
    String worldName;

    public PrismWorld(PrismServer prismServer, String worldName) {
        this.prismServer = prismServer;
        this.worldName = worldName;
    }

    public PrismServer getPrismServer() {
        return prismServer;
    }

    public String getWorldName() {
        return worldName;
    }

}
