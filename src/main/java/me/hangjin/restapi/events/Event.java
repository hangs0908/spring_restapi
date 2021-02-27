package me.hangjin.restapi.events;

import lombok.*;
import me.hangjin.restapi.accounts.Account;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder //@Data 는 쓰지말자( EqulasAndHashCode 담겨져있다.) 그리고 equalsandhashcode는 기본적으로 모든 필드를 가지고 만들기 때문에
            //상호 참조로 쓰지 않는데 좋다. 그래서 위에서처럼 따로 표현한다.
@Entity
public class Event {

    @Id @GeneratedValue
    private Integer id;

    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location;
    private int basePrice;
    private int maxPrice;
    private int limitEnrollment;
    private boolean offline;
    private boolean free;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    @ManyToOne
    private Account manager; //이벤트에서만 account를 참조 할수있도록

   public void update() {

        if(this.basePrice == 0 && this.maxPrice == 0) {
            this.free = true;
        } else {
            this.free = false;
        }

        //Offline Update
        if(this.location == null || this.location.isBlank()) {
            this.offline = false;
        } else {
            this.offline = true;
        }
    }
}
