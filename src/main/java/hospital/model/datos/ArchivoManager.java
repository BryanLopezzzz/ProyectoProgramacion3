package hospital.model.datos;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.beans.*;
import java.io.*;
import java.time.LocalDate;

public class ArchivoManager<T> {
    private final Class<T> type;

    public ArchivoManager(Class<T> type) {
        this.type = type;
    }

    public void saveList(List<T> lista, String archivo) throws Exception {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(archivo)))) {
            encoder.setPersistenceDelegate(LocalDate.class,
                    new DefaultPersistenceDelegate(new String[]{"year","month","dayOfMonth"}) {
                        @Override
                        protected Expression instantiate(Object oldInstance, Encoder out) {
                            LocalDate date = (LocalDate) oldInstance;
                            return new Expression(date, LocalDate.class, "of",
                                    new Object[]{date.getYear(), date.getMonthValue(), date.getDayOfMonth()});
                        }
                    });
            encoder.writeObject(lista);
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> loadList(String archivo) throws Exception {
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(archivo)))) {
            return (List<T>) decoder.readObject();
        }
    }
}
