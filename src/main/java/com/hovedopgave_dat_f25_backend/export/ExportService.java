package com.hovedopgave_dat_f25_backend.export;

import com.hovedopgave_dat_f25_backend.export_request.ExportRequest;
import com.hovedopgave_dat_f25_backend.flight.FlightDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportService {

    public byte[] handleExportData(ExportRequest exportRequest){

    }

    public byte[] exportFlightData(List<FlightDTO> flights, String fileFormat){
        if(fileFormat.equalsIgnoreCase("csv")){
            return exportToCsv(flights);
        } else {
            throw new UnsupportedOperationException("Invalid export format: " + fileFormat);
        }
    }

    private byte[] exportToCsv(List<FlightDTO> flights) {
        StringBuilder builder = new StringBuilder();
        builder.append("FlightNumber,DepartureTime,ArrivalTime\n");

        for(FlightDTO flight : flights) {
           builder
                   .append(flight.flightNumber())
                   .append(",")
                   .append(flight.departureTime())
                   .append(",")
                   .append(flight.departureTime())
                   .append("\n");
        }

        return builder.toString().getBytes();
    }
}
