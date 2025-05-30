package com.hovedopgave_dat_f25_backend.export;

import java.util.List;
import java.util.function.Function;

public record CsvExportStructure<E>(List<E> data, List<String> columnHeaderStructure,
                                    Function<E, List<String>> rowMapper) implements ExportStructure {

    public String buildData() {
        StringBuilder builder = new StringBuilder();

        builder.append(String.join(",", columnHeaderStructure)).append("\n");

        for (E item : data) {
            List<String> row = rowMapper.apply(item);
            builder.append(String.join(",", row)).append("\n");
        }

        return builder.toString();
    }
}
