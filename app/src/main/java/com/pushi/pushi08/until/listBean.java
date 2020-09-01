package com.pushi.pushi08.until;

import android.widget.ImageView;

public class listBean {
    private ImageView doctor_photo;
    private String doctor_name;
    private String doctor_title;
    private String consultation_room;
    private String consultation_room_synopsis;

    public ImageView getDoctor_photo() {
        return doctor_photo;
    }

    public void setDoctor_photo(ImageView doctor_photo) {
        this.doctor_photo = doctor_photo;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDoctor_title() {
        return doctor_title;
    }

    public void setDoctor_title(String doctor_title) {
        this.doctor_title = doctor_title;
    }

    public String getConsultation_room() {
        return consultation_room;
    }

    public void setConsultation_room(String consultation_room) {
        this.consultation_room = consultation_room;
    }

    public String getConsultation_room_synopsis() {
        return consultation_room_synopsis;
    }

    public void setConsultation_room_synopsis(String consultation_room_synopsis) {
        this.consultation_room_synopsis = consultation_room_synopsis;
    }

    @Override
    public String toString() {
        return "listBean{" +
                "doctor_photo=" + doctor_photo +
                ", doctor_name='" + doctor_name + '\'' +
                ", doctor_title='" + doctor_title + '\'' +
                ", consultation_room='" + consultation_room + '\'' +
                ", consultation_room_synopsis='" + consultation_room_synopsis + '\'' +
                '}';
    }
}
