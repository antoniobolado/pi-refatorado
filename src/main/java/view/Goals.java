
package view;

import security.Session;

/**
 *
 * @author anton
 */
public class Goals extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Goals.class.getName());

    /**
     * Creates new form Metas
     */
    public Goals() {
        initComponents();
        jPanelMercedes.setVisible(false);
        jPanel4.setVisible(false);
        jPanel5.setVisible(false);
        this.setTitle("Metas");
        if (Session.getInstance().isLoggedIn()) {
            carregarMetas();
}
        jLabelVerTudo.setVisible(false);
    }
    private void editarMetaDialog(model.Goal meta) {
    String novoNome = javax.swing.JOptionPane.showInputDialog(this, "Nome:", meta.getNome());
    if (novoNome == null) return;

    String novoObjetivo = javax.swing.JOptionPane.showInputDialog(this,
            "Valor objetivo:", String.format("%.2f", meta.getValorObjetivo()));
    if (novoObjetivo == null) return;

    String novoPrazo = javax.swing.JOptionPane.showInputDialog(this,
            "Prazo (Curto, Médio, Longo):", meta.getPrazo());
    if (novoPrazo == null) return;

    try {
        double obj = Double.parseDouble(novoObjetivo);
        meta.setNome(novoNome);
        meta.setValorObjetivo(obj);
        meta.setPrazo(novoPrazo);
        new model.GoalDAO().update(meta);
        carregarMetas();
    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Valor objetivo inválido.");
    }
}
    private void attachCardListeners(javax.swing.JPanel cardPanel, model.Goal meta) {
    // remover listeners anteriores para evitar múltiplos registros
    for (java.awt.event.MouseListener ml : cardPanel.getMouseListeners()) {
        cardPanel.removeMouseListener(ml);
    }

    cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            // clique simples = adicionar valor; duplo clique = editar meta
            if (evt.getClickCount() == 1) {
                // adicionar valor à meta
                String valorStr = javax.swing.JOptionPane.showInputDialog(
                        Goals.this,
                        "Adicionar valor à meta '" + meta.getNome() + "':",
                        "Adicionar Valor",
                        javax.swing.JOptionPane.PLAIN_MESSAGE);
                if (valorStr == null) return;
                try {
                    double valor = Double.parseDouble(valorStr);
                    meta.setValorAtual(meta.getValorAtual() + valor);
                    new model.GoalDAO().update(meta);
                    carregarMetas();
                } catch (NumberFormatException e) {
                    javax.swing.JOptionPane.showMessageDialog(Goals.this, "Valor inválido.");
                }
            } else if (evt.getClickCount() == 2) {
                // editar meta (nome, objetivo, prazo)
                editarMetaDialog(meta);
            }
        }
    });
}
    
    private void carregarMetas() {
    // garante que os 3 cards começam escondidos (você disse que já são false no initComponents,
    // repetimos aqui apenas por segurança)
    jPanelMercedes.setVisible(false);
    jPanel4.setVisible(false);
    jPanel5.setVisible(false);

    // Label "Ver tudo" que leva ao MetasCRUD (adicione este JLabel no seu form se ainda não existir)
    // Ex: private javax.swing.JLabel jLabelVerTudo;
    // e no initComponents: jLabelVerTudo.setVisible(false);
    jLabelVerTudo.setVisible(false);

    // Verifica sessão
    if (!security.Session.getInstance().isLoggedIn()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Usuário não autenticado.");
        return;
    }

    int usuarioId = security.Session.getInstance().getUsuario().getId();
    java.util.List<model.Goal> metas = new model.GoalDAO().listarPorUsuario(usuarioId);

    // Se não tiver metas, mostrar uma mensagem curta (opcional)
    if (metas.isEmpty()) {
        // você pode mostrar um texto no lugar dos cards ou manter tudo escondido
        return;
    }

    // Mostra até 3 metas nos cards manualmente
    for (int i = 0; i < Math.min(3, metas.size()); i++) {
        model.Goal m = metas.get(i);
        switch (i) {
            case 0:
                jPanelMercedes.setVisible(true);
                atualizarCard(m, jLabel7, jLabel8, jProgressBarMercedes);
                // adiciona listeners (ex: duplo clique abre edição ou adicionar valor)
                attachCardListeners(jPanelMercedes, m);
                break;
            case 1:
                jPanel4.setVisible(true);
                atualizarCard(m, jLabel9, jLabel10, jProgressBar2);
                attachCardListeners(jPanel4, m);
                break;
            case 2:
                jPanel5.setVisible(true);
                atualizarCard(m, jLabel11, jLabel12, jProgressBar3);
                attachCardListeners(jPanel5, m);
                break;
        }
    }

    // Se houver mais que 3 metas, habilita "Ver tudo" para abrir MetasCRUD
    if (metas.size() > 3) {
        jLabelVerTudo.setVisible(true);
        jLabelVerTudo.setText("Ver tudo (" + metas.size() + ")");
        jLabelVerTudo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelVerTudo.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                GoalsCRUD crud = new GoalsCRUD();
                crud.setLocationRelativeTo(Goals.this);
                crud.setVisible(true);
            }
        });
    }
}
    
    private void atualizarCard(model.Goal meta,
                           javax.swing.JLabel nomeLabel,
                           javax.swing.JLabel valorLabel,
                           javax.swing.JProgressBar progressBar) {

    nomeLabel.setText(meta.getNome());

    double percentual = (meta.getValorAtual() / meta.getValorObjetivo()) * 100;
    int progresso = (int) Math.min(percentual, 100);

    valorLabel.setText("R$" + String.format("%.2f", meta.getValorObjetivo()));

    progressBar.setValue(progresso);
    progressBar.setString(progresso + "%");

    if (progresso >= 100) {
        progressBar.setForeground(new java.awt.Color(15, 86, 28));
    }
}
    private void criarMeta() {

    String nome = javax.swing.JOptionPane.showInputDialog("Nome da meta:");
    String valorStr = javax.swing.JOptionPane.showInputDialog("Valor objetivo:");
    String prazo = javax.swing.JOptionPane.showInputDialog("Prazo (Curto, Médio, Longo):");

    if (nome == null || valorStr == null || prazo == null) return;

    try {
        double valor = Double.parseDouble(valorStr);

        model.Goal meta = new model.Goal();
        meta.setNome(nome);
        meta.setValorObjetivo(valor);
        meta.setValorAtual(0);
        meta.setPrazo(prazo);
        meta.setUsuario(Session.getInstance().getUsuario());

        new model.GoalDAO().create(meta);

        javax.swing.JOptionPane.showMessageDialog(this, "Meta criada!");
        carregarMetas();

    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Valor inválido.");
    }
}
    
    private void adicionarValorMeta(model.Goal meta) {

    String valorStr = javax.swing.JOptionPane.showInputDialog("Adicionar valor:");

    if (valorStr == null) return;

    try {
        double valor = Double.parseDouble(valorStr);

        meta.setValorAtual(meta.getValorAtual() + valor);

        new model.GoalDAO().update(meta);

        carregarMetas();

    } catch (NumberFormatException e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Valor inválido.");
    }
}
    private void excluirMeta(model.Goal meta) {

    int confirm = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Excluir meta?",
            "Confirmar",
            javax.swing.JOptionPane.YES_NO_OPTION);

    if (confirm == javax.swing.JOptionPane.YES_OPTION) {
        new model.GoalDAO().delete(meta.getId());
        carregarMetas();
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
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanelMercedes = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jProgressBarMercedes = new javax.swing.JProgressBar();
        jPanel4 = new javax.swing.JPanel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jProgressBar2 = new javax.swing.JProgressBar();
        jPanel5 = new javax.swing.JPanel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jProgressBar3 = new javax.swing.JProgressBar();
        jPanel6 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabelVerTudo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(10, 13, 15));

        jSeparator2.setBackground(new java.awt.Color(15, 86, 28));
        jSeparator2.setForeground(new java.awt.Color(15, 86, 28));

        jLabel4.setFont(new java.awt.Font("Bree Serif", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Relatórios");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Bree Serif", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Metas");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel2.setFont(new java.awt.Font("Bree Serif", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Registros");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Jacquard 24", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Illex");

        jLabel5.setFont(new java.awt.Font("Bree Serif", 2, 14)); // NOI18N
        jLabel5.setText("Sair");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel5MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(28, 28, 28))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel2.setBackground(new java.awt.Color(28, 28, 28));

        jLabel6.setFont(new java.awt.Font("Bree Serif", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(245, 235, 245));
        jLabel6.setText("Metas recentes");

        jPanelMercedes.setBackground(new java.awt.Color(25, 120, 42));
        jPanelMercedes.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, null));
        jPanelMercedes.setPreferredSize(new java.awt.Dimension(150, 150));
        jPanelMercedes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelMercedesMouseClicked(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Bree Serif", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Mercedes");

        jLabel8.setFont(new java.awt.Font("Bree Serif", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("R$1.7m");

        jSeparator3.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator3.setForeground(new java.awt.Color(255, 255, 255));

        jProgressBarMercedes.setBackground(new java.awt.Color(45, 45, 45));
        jProgressBarMercedes.setFont(new java.awt.Font("Bree Serif", 0, 10)); // NOI18N
        jProgressBarMercedes.setForeground(new java.awt.Color(34, 139, 34));
        jProgressBarMercedes.setBorder(null);
        jProgressBarMercedes.setStringPainted(true);

        javax.swing.GroupLayout jPanelMercedesLayout = new javax.swing.GroupLayout(jPanelMercedes);
        jPanelMercedes.setLayout(jPanelMercedesLayout);
        jPanelMercedesLayout.setHorizontalGroup(
            jPanelMercedesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMercedesLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelMercedesLayout.createSequentialGroup()
                .addGroup(jPanelMercedesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMercedesLayout.createSequentialGroup()
                        .addGroup(jPanelMercedesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelMercedesLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7))
                            .addGroup(jPanelMercedesLayout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(jLabel8)))
                        .addGap(0, 13, Short.MAX_VALUE))
                    .addGroup(jPanelMercedesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jProgressBarMercedes, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelMercedesLayout.setVerticalGroup(
            jPanelMercedesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMercedesLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarMercedes, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(156, 37, 37));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, null));
        jPanel4.setPreferredSize(new java.awt.Dimension(150, 150));

        jSeparator4.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator4.setForeground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Bree Serif", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText(" Dívida ");

        jLabel10.setFont(new java.awt.Font("Bree Serif", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("R$67k");

        jProgressBar2.setBackground(new java.awt.Color(45, 45, 45));
        jProgressBar2.setFont(new java.awt.Font("Bree Serif", 0, 10)); // NOI18N
        jProgressBar2.setForeground(new java.awt.Color(34, 139, 34));
        jProgressBar2.setBorder(null);
        jProgressBar2.setStringPainted(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel10)
                        .addGap(0, 51, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(52, 44, 158));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.white, null));
        jPanel5.setPreferredSize(new java.awt.Dimension(150, 150));

        jSeparator5.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator5.setForeground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Bree Serif", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Casa nova");

        jLabel12.setFont(new java.awt.Font("Bree Serif", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("R$589k");

        jProgressBar3.setBackground(new java.awt.Color(45, 45, 45));
        jProgressBar3.setFont(new java.awt.Font("Bree Serif", 0, 10)); // NOI18N
        jProgressBar3.setForeground(new java.awt.Color(34, 139, 34));
        jProgressBar3.setBorder(null);
        jProgressBar3.setStringPainted(true);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jProgressBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel12)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(6, 28, Short.MAX_VALUE)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)))
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setToolTipText("Pesquise aqui");

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton1.setText("Nova meta");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabelVerTudo.setText("Ver tudo");
        jLabelVerTudo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelVerTudoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanelMercedes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelVerTudo)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jLabelVerTudo))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelMercedes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
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

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {                                     
        new Registers().setVisible(true);
        this.dispose();
    }                                    

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {                                     
        new Reports().setVisible(true);
        this.dispose();
    }                                    

    private void jLabel5MouseEntered(java.awt.event.MouseEvent evt) {                                     
    }                                    

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {                                     
        Session.getInstance().logout();
        new HomeScreen().setVisible(true);
        this.dispose();
    }                                    

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        criarMeta();
    }                                        

    private void jPanelMercedesMouseClicked(java.awt.event.MouseEvent evt) {                                            
       
    }                                           

    private void jLabelVerTudoMouseClicked(java.awt.event.MouseEvent evt) {                                           

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
        java.awt.EventQueue.invokeLater(() -> new Goals().setVisible(true));
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelVerTudo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelMercedes;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JProgressBar jProgressBar3;
    private javax.swing.JProgressBar jProgressBarMercedes;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration                   
}
