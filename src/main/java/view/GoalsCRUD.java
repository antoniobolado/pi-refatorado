package view;

import security.Session;
import model.Goal;
import model.GoalDAO;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingUtilities;

public class GoalsCRUD extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GoalsCRUD.class.getName());

    public GoalsCRUD() {
        initComponents();
        this.setTitle("Metas - Completo");
        prepararTabela();
        carregarTodasMetas();
        // conectar botão criar
        criarMeta.addActionListener(e -> criarMeta());
    }

    // monta o model da tabela manualmente
    private void prepararTabela() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Id", "Nome", "Objetivo (R$)", "Atual (R$)", "Prazo", "% Concluído"}, 0) {

            Class[] types = new Class[]{Integer.class, String.class, Double.class, Double.class, String.class, Integer.class};
            boolean[] canEdit = new boolean[]{false, false, false, false, false, false};

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return canEdit[column];
            }
        };

        jTable1.setModel(model);

        // menu contexto: editar / excluir
        javax.swing.JPopupMenu popup = new javax.swing.JPopupMenu();
        javax.swing.JMenuItem editar = new javax.swing.JMenuItem("Editar");
        javax.swing.JMenuItem excluir = new javax.swing.JMenuItem("Excluir");

        editar.addActionListener(e -> editarMetaSelecionada());
        excluir.addActionListener(e -> excluirMetaSelecionada());

        popup.add(editar);
        popup.add(excluir);

        jTable1.setComponentPopupMenu(popup);

        // duplo clique para editar
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editarMetaSelecionada();
                }
            }
        });

        // layout visual (opcional): esconda coluna id se quiser
        jTable1.getColumnModel().getColumn(0).setMinWidth(50);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(60);
    }

    private void carregarTodasMetas() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        if (!Session.getInstance().isLoggedIn()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Usuário não autenticado.");
            return;
        }

        int usuarioId = Session.getInstance().getUsuario().getId();
        List<Goal> metas = new GoalDAO().listarPorUsuario(usuarioId);

        for (Goal m : metas) {
            int percentual = 0;
            if (m.getValorObjetivo() > 0) {
                percentual = (int) Math.min(100, Math.round((m.getValorAtual() / m.getValorObjetivo()) * 100));
            }
            model.addRow(new Object[]{
                m.getId(),
                m.getNome(),
                m.getValorObjetivo(),
                m.getValorAtual(),
                m.getPrazo(),
                percentual
            });
        }
    }

    private void criarMeta() {
        String nome = javax.swing.JOptionPane.showInputDialog(this, "Nome da meta:");
        if (nome == null || nome.trim().isEmpty()) return;

        String valorStr = javax.swing.JOptionPane.showInputDialog(this, "Valor objetivo (ex: 1000.00):");
        if (valorStr == null) return;

        String prazo = javax.swing.JOptionPane.showInputDialog(this, "Prazo (Curto, Médio, Longo):");
        if (prazo == null) return;

        try {
            double objetivo = Double.parseDouble(valorStr);
            Goal meta = new Goal();
            meta.setNome(nome.trim());
            meta.setValorObjetivo(objetivo);
            meta.setValorAtual(0.0);
            meta.setPrazo(prazo);
            meta.setUsuario(Session.getInstance().getUsuario());

            new GoalDAO().create(meta);
            javax.swing.JOptionPane.showMessageDialog(this, "Meta criada com sucesso!");
            carregarTodasMetas();
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Valor objetivo inválido.");
        }
    }

    private void editarMetaSelecionada() {
        int viewRow = jTable1.getSelectedRow();
        if (viewRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecione uma meta para editar.");
            return;
        }
        int row = jTable1.convertRowIndexToModel(viewRow);
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int id = (int) model.getValueAt(row, 0);

        // buscar meta pelo id na lista do usuário
        int usuarioId = Session.getInstance().getUsuario().getId();
        GoalDAO dao = new GoalDAO();
        Goal meta = dao.listarPorUsuario(usuarioId)
                       .stream()
                       .filter(m -> m.getId() == id)
                       .findFirst()
                       .orElse(null);

        if (meta == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "Meta não encontrada.");
            return;
        }

        String novoNome = javax.swing.JOptionPane.showInputDialog(this, "Nome:", meta.getNome());
        if (novoNome == null) return;

        String novoObjStr = javax.swing.JOptionPane.showInputDialog(this, "Valor objetivo:", String.format("%.2f", meta.getValorObjetivo()));
        if (novoObjStr == null) return;

        String novoAtualStr = javax.swing.JOptionPane.showInputDialog(this, "Valor atual:", String.format("%.2f", meta.getValorAtual()));
        if (novoAtualStr == null) return;

        String novoPrazo = javax.swing.JOptionPane.showInputDialog(this, "Prazo (Curto, Médio, Longo):", meta.getPrazo());
        if (novoPrazo == null) return;

        try {
            double novoObj = Double.parseDouble(novoObjStr);
            double novoAtual = Double.parseDouble(novoAtualStr);

            meta.setNome(novoNome.trim());
            meta.setValorObjetivo(novoObj);
            meta.setValorAtual(novoAtual);
            meta.setPrazo(novoPrazo);

            dao.update(meta);
            javax.swing.JOptionPane.showMessageDialog(this, "Meta atualizada!");
            carregarTodasMetas();
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Valores inválidos.");
        }
    }

    private void excluirMetaSelecionada() {
        int viewRow = jTable1.getSelectedRow();
        if (viewRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecione uma meta para excluir.");
            return;
        }
        int row = jTable1.convertRowIndexToModel(viewRow);
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int id = (int) model.getValueAt(row, 0);

        int confirm = javax.swing.JOptionPane.showConfirmDialog(this, "Deseja realmente excluir a meta selecionada?", "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm != javax.swing.JOptionPane.YES_OPTION) return;

        new GoalDAO().delete(id);
        javax.swing.JOptionPane.showMessageDialog(this, "Meta excluída.");
        carregarTodasMetas();
    }

    // aqui vão os demais métodos gerados pelo NetBeans (initComponents, main, etc...)
    // keep the rest of the class as you had it


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        criarMeta = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Metas - Completo");

        criarMeta.setText("Criar Meta");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(criarMeta)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(criarMeta))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

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
        java.awt.EventQueue.invokeLater(() -> new GoalsCRUD().setVisible(true));
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton criarMeta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration                   
}
