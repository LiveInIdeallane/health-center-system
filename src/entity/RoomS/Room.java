package entity.RoomS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    private Integer id;
    private String room_floor;
    private Integer room_no;

    @Override
    public String toString() {
        return id + "\t" + room_floor + "\t" + room_no;
    }
}