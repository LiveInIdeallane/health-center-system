package service.BackdownS;

import entity.BackdownS.Backdown;

import java.util.List;

public interface BackdownService {

    boolean createBackdown(Backdown backdown);
    boolean updateBackdown(Backdown backdown);
    boolean deleteBackdown(Backdown backdown);
    Backdown findBackdownById(Integer id);
    List<Backdown> findAllBackdowns();


}
