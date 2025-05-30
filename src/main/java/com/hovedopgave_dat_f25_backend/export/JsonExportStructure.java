package com.hovedopgave_dat_f25_backend.export;

import java.util.List;
import java.util.function.Function;

public record JsonExportStructure<E>(List<E> data, List<String> entityFields,
                                     Function<E, List<String>> entityMapper) implements ExportStructure {

    public String buildData() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            E item = data.get(i);
            List<String> row = entityMapper.apply(item);
            builder.append("            {\n");
            for (int j = 0; j < entityFields.size(); j++) {
                String field = entityFields.get(j);
                String value = row.get(j);
                builder.append("                \"").append(field).append("\": \"").append(value).append("\"");
                if (j < entityFields.size() - 1) {
                    builder.append(",");
                }
                builder.append("\n");
            }
            builder.append("            }");
            if (i < data.size() - 1) {
                builder.append(",");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
