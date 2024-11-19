package example.interfaces;

import example.entity.RegleManuelle;

import java.util.List;

public interface IRegleManuelleService {
    RegleManuelle createRegleManuelle(RegleManuelle regleManuelle);
    RegleManuelle updateRegleManuelle(int id, RegleManuelle regleManuelle);
    void deleteRegleManuelle(int id);
    RegleManuelle getRegleManuelle(int id);
    List<RegleManuelle> getRegleManuelles();
}
