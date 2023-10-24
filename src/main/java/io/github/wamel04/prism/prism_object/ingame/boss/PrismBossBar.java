package io.github.wamel04.prism.prism_object.ingame.boss;

public class PrismBossBar {

    String id;
    String title;
    PrismBarColor color;
    PrismBarStyle style;
    int duration;

    /**
     * @param id 보스바의 id
     * @param title 보스바의 제목 (%remaining_time%으로 남은 시간 표시 가능)
     * @param color 보스바의 색상
     * @param style 보스바의 스타일
     * @param duration 보스바의 지속 시간 (-1일 시 무한)
     */
    public PrismBossBar(String id, String title, PrismBarColor color, PrismBarStyle style, int duration) {
        this.id = id;
        this.title = title;
        this.color = color;
        this.style = style;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public PrismBarColor getColor() {
        return color;
    }

    public PrismBarStyle getStyle() {
        return style;
    }

    public int getDuration() {
        return duration;
    }

}
