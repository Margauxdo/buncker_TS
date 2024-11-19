package example.interfaces;

import example.entity.RetourSecurite;

import java.util.List;

public interface IRetourSecuriteService {
    RetourSecurite createRetourSecurite(RetourSecurite retourSecurite);
    RetourSecurite updateRetourSecurite(int id, RetourSecurite retourSecurite);
    void deleteRetourSecurite(int id);
    RetourSecurite getRetourSecurite(int id);
    List<RetourSecurite> getAllRetourSecurites();

}
