
package view;

import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;
import security.Session;

/**
 *
 * @author anton
 */
public class Registers extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Registers.class.getName());

    /**
     * Creates new form Registros
     */
    public Registers() {
        initComponents();
        configurarTabelaRegistros();
        criarMenuContexto();
        this.setTitle("Registros Financeiros");
        if (security.Session.getInstance().isLoggedIn()) {
    carregarRegistros();
    
}
    jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            editarRegistro();
        }
    }
});
    }
    
    
private void editarRegistro() {

    int row = jTable1.getSelectedRow();
    if (row == -1) return;

    int id = (int) jTable1.getValueAt(row, 0);

    String novaDescricao = javax.swing.JOptionPane.showInputDialog("Nova descrição:");
    String novoValorStr = javax.swing.JOptionPane.showInputDialog("Novo valor:");

    if (novaDescricao == null || novoValorStr == null) return;

    try {
        double novoValor = Double.parseDouble(novoValorStr);

        model.RegisterDAO dao = new model.RegisterDAO();
        model.Register r =
                dao.listarPorUsuario(
                        security.Session.getInstance().getUsuario().getId()
                ).stream()
                 .filter(reg -> reg.getId() == id)
                 .findFirst()
                 .orElse(null);

        if (r != null) {
            r.setDescricao(novaDescricao);
            r.setValor(novoValor);
            r.setTipo(novoValor >= 0 ? "Entrada" : "Saída");

            dao.update(r);
            carregarRegistros();
        }

    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Valor inválido.");
    }
}
private void excluirRegistroSelecionado() {

    int row = jTable1.getSelectedRow();
    if (row == -1) return;

    int id = (int) jTable1.getValueAt(row, 0);

    int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente excluir?",
            "Confirmar",
            javax.swing.JOptionPane.YES_NO_OPTION
    );

    if (confirm == javax.swing.JOptionPane.YES_OPTION) {
        new model.RegisterDAO().delete(id);
        carregarRegistros();
    }
}
    private void criarMenuContexto() {

    javax.swing.JPopupMenu popup = new javax.swing.JPopupMenu();
    javax.swing.JMenuItem excluir = new javax.swing.JMenuItem("Excluir");

    excluir.addActionListener(e -> excluirRegistroSelecionado());

    popup.add(excluir);

    jTable1.setComponentPopupMenu(popup);
}
private void atualizarSaldo() {

    int usuarioId = security.Session.getInstance().getUsuario().getId();
    double saldo = new model.RegisterService().calcularSaldoPorUsuario(usuarioId);

    lblDescricao.setText("Saldo Atual:");

    String sinal = saldo >= 0 ? "+" : "";
    lblValor.setText(sinal + String.format("%.2f", saldo));

    lblValor.setForeground(
        saldo >= 0 ? new java.awt.Color(15, 86, 28)
                   : new java.awt.Color(200, 0, 0)
    );
}
    
private void carregarRegistros() {

    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0);

    int usuarioId = security.Session.getInstance().getUsuario().getId();

    model.RegisterDAO dao = new model.RegisterDAO();
    java.util.List<model.Register> lista =
            dao.listarPorUsuario(usuarioId);

    for (model.Register r : lista) {
        model.addRow(new Object[]{
            r.getId(),
            r.getDescricao(),
            r.getValor()
        });
    }

    atualizarSaldo();
}

    public void adicionarRegistroAoPainel(String descricao, double valor) {
    String sinal = (valor >= 0) ? "+" : "";
    java.awt.Color corValor = (valor >= 0) ? new java.awt.Color(15, 86, 28) : new java.awt.Color(200, 0, 0);
    
    // Se estiver usando labels para o "Balanço da semana":
    lblDescricao.setText(descricao);
    lblValor.setText(sinal + String.format("%.2f", valor));
    lblValor.setForeground(corValor);
}
    
    private void configurarTabelaRegistros() {
    // Cores do seu tema
    java.awt.Color darkBg = new java.awt.Color(10, 13, 15);
    java.awt.Color fontWhite = new java.awt.Color(255, 255, 255);

    jTable1.setBackground(darkBg);
    jTable1.setForeground(fontWhite);
    jTable1.setGridColor(new java.awt.Color(40, 40, 40));
    jTable1.setRowHeight(30);
    jTable1.setSelectionBackground(new java.awt.Color(15, 86, 28)); // Verde Illex ao selecionar
    
    // remove a borda da tabela
    jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    jScrollPane1.getViewport().setBackground(darkBg);
    

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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        btnCriarRegistro = new javax.swing.JButton();
        lblDescricao = new javax.swing.JLabel();
        lblValor = new javax.swing.JLabel();

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
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jLabel1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 167, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel2.setBackground(new java.awt.Color(28, 28, 28));

        jTextField1.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nome", "Valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel6.setFont(new java.awt.Font("Bree Serif", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(244, 244, 244));
        jLabel6.setText("Registros:");

        btnCriarRegistro.setForeground(new java.awt.Color(144, 144, 144));
        btnCriarRegistro.setText("Crie um registro");
        btnCriarRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCriarRegistroActionPerformed(evt);
            }
        });

        lblDescricao.setForeground(new java.awt.Color(244, 244, 244));
        lblDescricao.setText("Saldo atual:");

        lblValor.setForeground(new java.awt.Color(244, 244, 244));
        lblValor.setText("xx.xx");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCriarRegistro))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblDescricao)
                                .addGap(120, 120, 120)
                                .addComponent(lblValor)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDescricao)
                    .addComponent(lblValor))
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(btnCriarRegistro))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
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

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {                                     
        new Reports().setVisible(true);
        this.dispose();
    }                                    

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {                                     
        Session.getInstance().logout();
        new HomeScreen().setVisible(true);
        this.dispose();
    }                                    

    private void btnCriarRegistroActionPerformed(java.awt.event.ActionEvent evt) {                                                 


    if (!security.Session.getInstance().isLoggedIn()) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Usuário não autenticado.");
        return;
    }

    String descricao = javax.swing.JOptionPane.showInputDialog("Descrição:");
    String valorStr = javax.swing.JOptionPane.showInputDialog("Valor (use negativo para saída):");

    if (descricao == null || valorStr == null) return;

    try {
        double valor = Double.parseDouble(valorStr);

        model.Register registro = new model.Register();
        registro.setDescricao(descricao);
        registro.setValor(valor);
        registro.setTipo(valor >= 0 ? "Entrada" : "Saída");
        registro.setData(new java.util.Date());
        registro.setCategoria("Geral");
        registro.setUsuario(security.Session.getInstance().getUsuario());

        new model.RegisterDAO().create(registro);

        javax.swing.JOptionPane.showMessageDialog(this,
                "Registro salvo com sucesso!");

        carregarRegistros();

    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this,
                "Valor inválido.");
    
}
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
        java.awt.EventQueue.invokeLater(() -> new Registers().setVisible(true));
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnCriarRegistro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblDescricao;
    private javax.swing.JLabel lblValor;
    // End of variables declaration                   
}
