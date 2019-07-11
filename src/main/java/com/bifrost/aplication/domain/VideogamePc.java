package com.bifrost.aplication.domain;

import javax.persistence.*;

@Entity
@Table(name = "videogames_pc")
public class VideogamePc {

        @Id
        @Column(name = "id_videogame_pc")
        @GeneratedValue
        private Integer id_videogame_pc;

        private String videogame_pc_name;

        private String videogame_pc_type;

        private Integer videogame_pc_year;

        private String platform_name;

        private String company_name;

        private Integer is_digital;

        private Integer is_completed;

        private Integer pretended;

        public String getVideogame_pc_name() {
                return videogame_pc_name;
        }

        public void setVideogame_pc_name(String videogame_pc_name) {
                this.videogame_pc_name = videogame_pc_name;
        }

        public String getVideogame_pc_type() {
                return videogame_pc_type;
        }

        public void setVideogame_pc_type(String videogame_pc_type) {
                this.videogame_pc_type = videogame_pc_type;
        }

        public Integer getVideogame_pc_year() {
                return videogame_pc_year;
        }

        public void setVideogame_pc_year(Integer videogame_pc_year) {
                this.videogame_pc_year = videogame_pc_year;
        }

        public String getPlatform_name() {
                return platform_name;
        }

        public void setPlatform_name(String platform_name) {
                this.platform_name = platform_name;
        }

        public String getCompany_name() {
                return company_name;
        }

        public void setCompany_name(String company_name) {
                this.company_name = company_name;
        }

        public Integer getIs_digital() {
                return is_digital;
        }

        public void setIs_digital(Integer is_digital) {
                this.is_digital = is_digital;
        }

        public Integer getIs_completed() {
                return is_completed;
        }

        public void setIs_completed(Integer is_completed) {
                this.is_completed = is_completed;
        }

        public Integer getPretended() {
                return pretended;
        }

        public void setPretended(Integer pretended) {
                this.pretended = pretended;
        }
}
