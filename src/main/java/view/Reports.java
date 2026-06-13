
package view;

import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import security.Session;

/**
 *
 * @author anton
 */
public class Reports extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Reports.class.getName());

    /**
     * Creates new form Relatorios
     */
    public Reports() {
        initComponents();
        configurarGraficoReal();
        this.setTitle("Relatorios");
        relatorioCompleto.addActionListener(e -> gerarRelatorioCompleto());
        personalizarRelatorio.addActionListener(e -> gerarRelatorioPersonalizado());
        configurarGraficoReal();
        jSplitPane1.setResizeWeight(0.2); // Dá 20% de espaço para o menu e 80% para o conteúdo
        jSplitPane1.setDividerLocation(150); // Define uma largura fixa inicial para o menu lateral
    }


private void configurarGraficoReal() {

    if (!Session.getInstance().isLoggedIn()) return;

    int usuarioId = Session.getInstance().getUsuario().getId();
    java.util.List<model.Register> registros =
            new model.RegisterDAO().listarPorUsuario(usuarioId);

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    if (registros.isEmpty()) {
        dataset.addValue(0, "Saldo", "Sem dados");
    } else {

        // pega últimos 5 registros
        int start = Math.max(0, registros.size() - 5);

        double saldoAcumulado = 0;

        for (int i = start; i < registros.size(); i++) {
            model.Register r = registros.get(i);
            saldoAcumulado += r.getValor();
            dataset.addValue(saldoAcumulado, "Saldo",
                    "Reg " + (i + 1));
        }
    }

    JFreeChart chart = ChartFactory.createLineChart(
            null, null, null,
            dataset,
            PlotOrientation.VERTICAL,
            false, false, false
    );

    java.awt.Color corFundo = new java.awt.Color(10, 13, 15);
    chart.setBackgroundPaint(corFundo);

    CategoryPlot plot = chart.getCategoryPlot();
    plot.setBackgroundPaint(corFundo);
    plot.setRangeGridlinePaint(java.awt.Color.GRAY);
    plot.setOutlineVisible(false);

    ChartPanel chartPanel = new ChartPanel(chart);
    // ADICIONE ESTAS LINHAS:
    chartPanel.setPreferredSize(new java.awt.Dimension(400, 200)); // Define um tamanho base
    chartPanel.setMouseWheelEnabled(true); // Opcional: permite zoom com o scroll
    
    seuJPanelDoGrafico.removeAll();
    seuJPanelDoGrafico.setLayout(new java.awt.BorderLayout());
    seuJPanelDoGrafico.add(chartPanel, java.awt.BorderLayout.CENTER);
    
    // Força o componente a respeitar o tamanho no GroupLayout
    seuJPanelDoGrafico.revalidate(); 
    seuJPanelDoGrafico.repaint();
}

private void gerarRelatorioCompleto() {
    gerarCSVRegistros();
    gerarRelatorioMetasMD();
    javax.swing.JOptionPane.showMessageDialog(this,
            "Relatório completo gerado com sucesso!");
}

private void gerarCSVRegistros() {

    if (!Session.getInstance().isLoggedIn()) return;

    int usuarioId = Session.getInstance().getUsuario().getId();
    java.util.List<model.Register> registros =
            new model.RegisterDAO().listarPorUsuario(usuarioId);

    try (java.io.FileWriter writer = new java.io.FileWriter("registros.csv")) {

        writer.write("Descricao,Valor,Data\n");

        double total = 0;

        for (model.Register r : registros) {
            writer.write(r.getDescricao() + "," +
                    r.getValor() + "," +
                    r.getData() + "\n");
            total += r.getValor();
        }

        writer.write("\nBALANCO FINAL," + total + ",\n");

    } catch (java.io.IOException e) {
        e.printStackTrace();
    }
}


private void gerarRelatorioMetasMD() {

    if (!Session.getInstance().isLoggedIn()) return;

    int usuarioId = Session.getInstance().getUsuario().getId();
    java.util.List<model.Goal> metas =
            new model.GoalDAO().listarPorUsuario(usuarioId);

    try (java.io.FileWriter writer = new java.io.FileWriter("metas.md")) {

        writer.write("# Relatório de Metas\n\n");

        for (model.Goal m : metas) {

            double percentual = 0;
            if (m.getValorObjetivo() > 0) {
                percentual = (m.getValorAtual() / m.getValorObjetivo()) * 100;
            }

            writer.write("## " + m.getNome() + "\n");
            writer.write("- Objetivo: R$ " + m.getValorObjetivo() + "\n");
            writer.write("- Atual: R$ " + m.getValorAtual() + "\n");
            writer.write("- Prazo: " + m.getPrazo() + "\n");
            writer.write("- Conclusão: " + String.format("%.1f", percentual) + "%\n");
            writer.write("\n---\n\n");
        }

    } catch (java.io.IOException e) {
        e.printStackTrace();
    }
}

private void gerarRelatorioPersonalizado() {

    String[] opcoes = {"Somente Registros (CSV)",
                       "Somente Metas (MD)"};

    int escolha = javax.swing.JOptionPane.showOptionDialog(
            this,
            "O que deseja gerar?",
            "Relatório Personalizado",
            javax.swing.JOptionPane.DEFAULT_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE,
            null,
            opcoes,
            opcoes[0]);

    if (escolha == 0) {
        gerarCSVRegistros();
        javax.swing.JOptionPane.showMessageDialog(this,
                "CSV gerado com sucesso!");
    } else if (escolha == 1) {
        gerarRelatorioMetasMD();
        javax.swing.JOptionPane.showMessageDialog(this,
                "Relatório de metas gerado!");
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        seuJPanelDoGrafico = new javax.swing.JPanel();
        seuJPanelDoGrafico1 = new javax.swing.JPanel();
        personalizarRelatorio = new javax.swing.JButton();
        relatorioCompleto = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(10, 13, 15));

        jLabel1.setFont(new java.awt.Font("Jacquard 24", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Illex");

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Bree Serif", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Registros");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Bree Serif", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Metas");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bree Serif", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Relatórios");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jSeparator2.setBackground(new java.awt.Color(15, 86, 28));
        jSeparator2.setForeground(new java.awt.Color(15, 86, 28));

        jLabel5.setFont(new java.awt.Font("Bree Serif", 2, 14)); // NOI18N
        jLabel5.setText("Sair");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(28, 28, 28))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 186, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel2.setBackground(new java.awt.Color(26, 26, 26));
        jPanel2.setAutoscrolls(true);

        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setText("Pesquise aqui");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        seuJPanelDoGrafico.setBackground(new java.awt.Color(10, 13, 15));
        seuJPanelDoGrafico.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout seuJPanelDoGraficoLayout = new javax.swing.GroupLayout(seuJPanelDoGrafico);
        seuJPanelDoGrafico.setLayout(seuJPanelDoGraficoLayout);
        seuJPanelDoGraficoLayout.setHorizontalGroup(
            seuJPanelDoGraficoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 434, Short.MAX_VALUE)
        );
        seuJPanelDoGraficoLayout.setVerticalGroup(
            seuJPanelDoGraficoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 177, Short.MAX_VALUE)
        );

        seuJPanelDoGrafico1.setBackground(new java.awt.Color(10, 13, 15));
        seuJPanelDoGrafico1.setForeground(new java.awt.Color(255, 255, 255));

        personalizarRelatorio.setBackground(new java.awt.Color(248, 250, 252));
        personalizarRelatorio.setFont(new java.awt.Font("Bree Serif", 0, 14)); // NOI18N
        personalizarRelatorio.setForeground(new java.awt.Color(18, 10, 12));
        personalizarRelatorio.setText("Personalizar");

        relatorioCompleto.setBackground(new java.awt.Color(15, 86, 28));
        relatorioCompleto.setFont(new java.awt.Font("Bree Serif", 1, 18)); // NOI18N
        relatorioCompleto.setForeground(new java.awt.Color(244, 244, 244));
        relatorioCompleto.setText("Relatório completo");

        javax.swing.GroupLayout seuJPanelDoGrafico1Layout = new javax.swing.GroupLayout(seuJPanelDoGrafico1);
        seuJPanelDoGrafico1.setLayout(seuJPanelDoGrafico1Layout);
        seuJPanelDoGrafico1Layout.setHorizontalGroup(
            seuJPanelDoGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(seuJPanelDoGrafico1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(relatorioCompleto, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(personalizarRelatorio, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        seuJPanelDoGrafico1Layout.setVerticalGroup(
            seuJPanelDoGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, seuJPanelDoGrafico1Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(seuJPanelDoGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(relatorioCompleto, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(personalizarRelatorio, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        jLabel6.setFont(new java.awt.Font("Bree Serif", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(245, 235, 245));
        jLabel6.setText("Relatório de Janeiro");

        jLabel7.setFont(new java.awt.Font("Bree Serif", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(245, 235, 245));
        jLabel7.setText("Outras possibilidades");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(seuJPanelDoGrafico1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(seuJPanelDoGrafico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(seuJPanelDoGrafico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(seuJPanelDoGrafico1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jSplitPane1.setRightComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>                        

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {                                     
        new Goals().setVisible(true);
        this.dispose();
    }                                    

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {                                     
        new Registers().setVisible(true);
        this.dispose();
    }                                    

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {                                     
        Session.getInstance().logout();
        new HomeScreen().setVisible(true);
        this.dispose();
    }                                    

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Reports().setVisible(true));
    }

    // Variables declaration - do not modify                     
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton personalizarRelatorio;
    private javax.swing.JButton relatorioCompleto;
    private javax.swing.JPanel seuJPanelDoGrafico;
    private javax.swing.JPanel seuJPanelDoGrafico1;
    // End of variables declaration                   
}
