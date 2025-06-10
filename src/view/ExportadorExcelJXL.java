package view;

import java.io.File;
import java.util.List;
import jxl.Workbook;
import jxl.write.*;

public class ExportadorExcelJXL {

    public static void exportarVendas(List<model.Movimento> lista, File arquivo) throws Exception {
        WritableWorkbook workbook = Workbook.createWorkbook(arquivo);
        WritableSheet sheet = workbook.createSheet("Vendas", 0);

        // Cabeçalho
        sheet.addCell(new Label(0, 0, "ID"));
        sheet.addCell(new Label(1, 0, "ID Caixa"));
        sheet.addCell(new Label(2, 0, "Tipo"));
        sheet.addCell(new Label(3, 0, "Valor"));
        sheet.addCell(new Label(4, 0, "Descricao"));
        sheet.addCell(new Label(5, 0, "Data/Hora"));

        int row = 1;
        for (model.Movimento m : lista) {
            sheet.addCell(new jxl.write.Number(0, row, m.getId()));
            sheet.addCell(new jxl.write.Number(1, row, m.getIdCaixa()));
            sheet.addCell(new Label(2, row, m.getTipo()));
            sheet.addCell(new jxl.write.Number(3, row, m.getValor()));
            sheet.addCell(new Label(4, row, m.getDescricao()));
            sheet.addCell(new Label(5, row, m.getDataHora().toString()));
            row++;
        }

        workbook.write();
        workbook.close();
    }
}