# WebService-Restful-Java

# Capitulo 4: Banco de dados com Java

Neste capitulo vemos a estruturação do banco de dados e de começo aprendemos a usar a API clássica "JDBC" do java. Futuramente vamos estudar o framework Hibernate de persistência para aumentar a produtividade.

### JDBC

A API JDBC (java Database Connectivity) permite conectar em qualquer  banco de dados utilizando a linguagem Java, sendo que para realizar a conexão é necessário um driver JDBC, que faz o ******************************************elo de comunicação entre o código e o banco de dados.****************************************** 

- Criamos 4 classes em um novo projeto, que se chama: ***br.com.livro.domain***:
  
    <details>
      <summary>BaseDAO</summary> 

    
    ```java
    package br.com.livro.domain;
    
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.SQLException;
    
    public class BaseDAO {
    	public BaseDAO() {
    		// Clase usada para a conecxão com JDBC
    		try {
    			// Necessário para utilizar o drive JDBC do Mysql
    			Class.forName("com.mysql.jdbc.Driver");
    		} catch (ClassNotFoundException e) {
    			// Erro de driver JDBC (adicione o drive .jar do MYSQL em /WEB-INF/lib
    			e.printStackTrace();
    		}
    	}
    
    	protected Connection getConnection() throws SQLException {
    		// URL de conexão com o banco de dados
    		String url = "jdbc:mysql://localhost/livro";
    
    		// Conecta utilizando a URL, usuario e senha.
    		Connection conn = DriverManager.getConnection(url, "livro", "livro123");
    		return conn;
    	}
    	public static void main(String[] args) throws SQLException {
    		BaseDAO db = new BaseDAO();
    		//testa a conexao
    		Connection conn = db.getConnection();
    		System.out.println(conn);
    	}
    }
    ```
   </details>
  <div></div>
  <details>
      <summary>Carro</summary>
 
    
    ```java
    package br.com.livro.domain;
    
    import java.io.Serializable;
    public class Carro implements Serializable {
    	private static final long serialVersionUID = 1L;
    	private Long id;
    	private String tipo;
    	private String nome;
    	private String desc;
    	private String urlFoto;
    	private String urlVideo;
    	private String latitude;
    	private String longitude;
    	public Long getId() {
    		return id;
    	}
    	public void setId(Long id) {
    		this.id = id;
    	}
    	public String getTipo() {
    		return tipo;
    	}
    	public void setTipo(String tipo) {
    		this.tipo = tipo;
    	}
    	public String getNome() {
    		return nome;
    	}
    	public void setNome(String nome) {
    		this.nome = nome;
    	}
    	public String getDesc() {
    		return desc;
    	}
    	public void setDesc(String desc) {
    		this.desc = desc;
    	}
    	public String getUrlFoto() {
    		return urlFoto;
    	}
    	public void setUrlFoto(String urlFoto) {
    		this.urlFoto = urlFoto;
    	}
    	public String getUrlVideo() {
    		return urlVideo;
    	}
    	public void setUrlVideo(String urlVideo) {
    		this.urlVideo = urlVideo;
    	}
    	public String getLatitude() {
    		return latitude;
    	}
    	public void setLatitude(String latitude) {
    		this.latitude = latitude;
    	}
    	public String getLongitude() {
    		return longitude;
    	}
    	public void setLongitude(String longitude) {
    		this.longitude = longitude;
    	}
    	@Override
    	public String toString() {
    		return "Carro [id=" + id + ", tipo=" + tipo + ", nome=" + nome + ", desc=" + desc + ", urlFoto=" + urlFoto + ", urlVideo=" + urlVideo + ", latitude=" + latitude + ", longitude=" + longitude + "]";
    	}
    }
    ```
  
   > Na classe carro é demonstrado uma implementação. Essa é que toda classe para permitir que seus objetos tenham a capacidade de serem serializados, deve “obrigatoriamente” implementar a interface java.io.Serializable. **Essa interface não possui nenhum método**. Ela é usada apenas para registrar a semântica de serialização para a **[máquina virtual Java](https://www.devmedia.com.br/introducao-ao-java-virtual-machine-jvm/27624)**, indicando que os objetos instanciados a partir da classe podem ser serializados (ou transformados em uma sequência de bytes).
   
  </details>
  <details>
      <summary>CarroDAO</summary>
      - CarroDAO
    
    ```java
    package br.com.livro.domain;
    
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.sql.Statement;
    import java.util.ArrayList;
    import java.util.List;
    
    public class CarroDAO extends BaseDAO {
    	
    	public Carro getCarroById(Long id) throws SQLException {
    		Connection conn = null;
    		PreparedStatement stmt = null;
    		try {
    			conn = getConnection();
    			stmt = conn.prepareStatement("select * from carro where id=?");
    			stmt.setLong(1, id);
    			ResultSet rs = stmt.executeQuery();
    			if (rs.next()) {
    				Carro c = createCarro(rs);
    				rs.close();
    				return c;
    			}
    		} finally {
    			if (stmt != null) {
    				stmt.close();
    			}
    			if (conn != null) {
    				conn.close();
    			}
    		}
    		return null;
    	}
    
    	public List<Carro> findByName(String name) throws SQLException {
    		List<Carro> carros = new ArrayList<>();
    		Connection conn = null;
    		PreparedStatement stmt = null;
    		try {
    			conn = getConnection();
    			stmt = conn.prepareStatement("select * from carro where lower(nome) like ?");
    			stmt.setString(1, "%" + name.toLowerCase() + "%");
    			ResultSet rs = stmt.executeQuery();
    			while (rs.next()) {
    				Carro c = createCarro(rs);
    				carros.add(c);
    			}
    			rs.close();
    		} finally {
    			if (stmt != null) {
    				stmt.close();
    			}
    			if (conn != null) {
    				conn.close();
    			}
    		}
    		return carros;
    	}
    
    	public List<Carro> findByTipo(String tipo) throws SQLException {
    		List<Carro> carros = new ArrayList<>();
    		Connection conn = null;
    		PreparedStatement stmt = null;
    		try {
    			conn = getConnection();
    			stmt = conn.prepareStatement("select * from carro where tipo = ?");
    			stmt.setString(1, tipo);
    			ResultSet rs = stmt.executeQuery();
    			while (rs.next()) {
    				Carro c = createCarro(rs);
    				carros.add(c);
    			}
    			rs.close();
    		} finally {
    			if (stmt != null) {
    				stmt.close();
    			}
    			if (conn != null) {
    				conn.close();
    			}
    		}
    		return carros;
    	}
    
    	public List<Carro> getCarros() throws SQLException {
    		List<Carro> carros = new ArrayList<>();
    		Connection conn = null;
    		PreparedStatement stmt = null;
    		try {
    			conn = getConnection();
    			stmt = conn.prepareStatement("select * from carro");
    			ResultSet rs = stmt.executeQuery();
    			while (rs.next()) {
    				Carro c = createCarro(rs);
    				carros.add(c);
    			}
    			rs.close();
    		} finally {
    			if (stmt != null) {
    				stmt.close();
    			}
    			if (conn != null) {
    				conn.close();
    			}
    		}
    		return carros;
    	}
    
    	public Carro createCarro(ResultSet rs) throws SQLException {
    		Carro c = new Carro();
    		c.setId(rs.getLong("id"));
    		c.setNome(rs.getString("nome"));
    		c.setDesc(rs.getString("descricao"));
    		c.setUrlFoto(rs.getString("url_foto"));
    		c.setUrlVideo(rs.getString("url_video"));
    		c.setLatitude(rs.getString("latitude"));
    		c.setLongitude(rs.getString("longitude"));
    		c.setTipo(rs.getString("tipo"));
    		return c;
    	}
    
    	public void save(Carro c) throws SQLException {
    		Connection conn = null;
    		PreparedStatement stmt = null;
    		try {
    			conn = getConnection();
    			if (c.getId() == null) {
    				stmt = conn.prepareStatement(
    						"insert into carro (nome,descricao,url_foto,url_video,latitude,longitude,tipo) VALUES(?,?,?,?,?,?,?)",
    						Statement.RETURN_GENERATED_KEYS);
    			} else {
    				stmt = conn.prepareStatement(
    						"update carro set nome=?,descricao=?,url_foto=?,url_video=?,latitude=?,longitude=?,tipo=? where id=?");
    			}
    			stmt.setString(1, c.getNome());
    			stmt.setString(2, c.getDesc());
    			stmt.setString(3, c.getUrlFoto());
    			stmt.setString(4, c.getUrlVideo());
    			stmt.setString(5, c.getLatitude());
    			stmt.setString(6, c.getLongitude());
    			stmt.setString(7, c.getTipo());
    			if (c.getId() != null) {
    				// Update
    				stmt.setLong(8, c.getId());
    			}
    			int count = stmt.executeUpdate();
    			if (count == 0) {
    				throw new SQLException("Erro ao inserir o carro");
    			}
    			// Se inseriu, ler o id auto incremento
    			if (c.getId() == null) {
    				Long id = getGeneratedId(stmt);
    				c.setId(id);
    			}
    		} finally {
    			if (stmt != null) {
    				stmt.close();
    			}
    			if (conn != null) {
    				conn.close();
    			}
    		}
    	}
    
    	// id gerado com o campo auto incremento
    	public static Long getGeneratedId(Statement stmt) throws SQLException {
    		ResultSet rs = stmt.getGeneratedKeys();
    		if (rs.next()) {
    			Long id = rs.getLong(1);
    			return id;
    		}
    		return 0L;
    	}
    
    	public boolean delete(Long id) throws SQLException {
    		Connection conn = null;
    		PreparedStatement stmt = null;
    		try {
    			conn = getConnection();
    			stmt = conn.prepareStatement("delete from carro where id=?");
    			stmt.setLong(1, id);
    			int count = stmt.executeUpdate();
    			boolean ok = count > 0;
    			return ok;
    		} finally {
    			if (stmt != null) {
    				stmt.close();
    			}
    			if (conn != null) {
    				conn.close();
    			}
    		}
    	}
    }
    ```
  </details>
  <details>
      <summary>CarroService</summary>
    
  </details>

        
    
    # 4.2 Persistindo a classe carro
    
    > Nota: ******persistência de objetos****** é o termo utilizado para armazenar um objeto em qualquer fonte de dados, seja em um arquivo texto ou em um banco de dados. Portanto é comum encontrarmos esse termo na Orientação a Objetos.
    
    
    # MAS O QUE É SERIALIZAÇÃO?
    
    A serialização em Java é o processo de converter um objeto Java em uma sequência de bytes, de modo que esse objeto possa ser armazenado em um arquivo, transmitido por uma rede ou restaurado em um objeto Java posteriormente. A serialização é amplamente utilizada em Java para salvar o estado de objetos e permitir a transferência de objetos entre diferentes partes de um programa ou até mesmo entre aplicativos Java.
    
    A serialização é útil em cenários como:
    
    1. Armazenamento de estado de objetos em arquivos para persistência de dados.
    2. Transferência de objetos através de uma rede.
    3. Armazenamento de objetos em bancos de dados.
       

   <p> Todos os arquivos que forem selecionados à pasta /WEB-INF/lib são automaticamente adicionados ao classpath do projeto</p>
  

    ```markdown
    > O termo **classpath**  no Java indica a pasta na qual a JVM vai
     buscar as bibliotecas para executar o código. 
    ```
