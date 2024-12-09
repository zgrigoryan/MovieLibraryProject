package repository;

import model.*;
import java.util.*;

public class GuestRepository extends UserRepository {
    public List<Guest> getAllGuests() {
        List<Guest> guests = new ArrayList<>();
        for (User user : getAllUsers()) {
            if (user instanceof Guest) {
                guests.add((Guest) user);
            }
        }
        return guests;
    }
}
