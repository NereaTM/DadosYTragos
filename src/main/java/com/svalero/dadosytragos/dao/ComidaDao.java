package com.svalero.dadosytragos.dao;

import com.svalero.dadosytragos.domain.Comida;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ComidaDao {
    private final Connection conexion;

    public ComidaDao(Connection conexion) {
        this.conexion = conexion;
    }

    public int agregar(Comida comida) throws SQLException {
        String sql = "INSERT INTO comidas (nombre, tipo, precio, stock, descripcion, es_vegetariana, fecha_registro) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            asignarParametros(ps, comida, false);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se inserto ninguna comida");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    comida.setIdComida(rs.getInt(1));
                    return comida.getIdComida();
                } else {
                    throw new SQLException("No se genero el ID de comida");
                }
            }
        }
    }

    public boolean actualizar(Comida comida) throws SQLException {
        String sql = "UPDATE comidas SET nombre=?, tipo=?, precio=?, stock=?, descripcion=?, "
                + "es_vegetariana=?, fecha_registro=? WHERE id_comida=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            asignarParametros(ps, comida, true);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idComida) throws SQLException {
        String sql = "DELETE FROM comidas WHERE id_comida = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idComida);
            return ps.executeUpdate() > 0;
        }
    }

    public Comida buscarPorId(int idComida) throws SQLException {
        String sql = "SELECT * FROM comidas WHERE id_comida = ?";
        List<Comida> lista = lanzarConsulta(sql, idComida);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public List<Comida> listarTodas() throws SQLException {
        String sql = "SELECT * FROM comidas";
        return lanzarConsulta(sql);
    }

    public List<Comida> buscarPorCriterios(String nombre,
                                           String tipo,
                                           Double precioMin,
                                           Double precioMax,
                                           Boolean esVegetariana) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM comidas WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (nombre != null && !nombre.isBlank()) {
            sql.append(" AND LOWER(nombre) LIKE LOWER(?)");
            params.add("%" + nombre + "%");
        }
        if (tipo != null && !tipo.isBlank()) {
            sql.append(" AND LOWER(tipo) LIKE LOWER(?)");
            params.add("%" + tipo + "%");
        }
        if (precioMin != null) {
            sql.append(" AND precio >= ?");
            params.add(precioMin);
        }
        if (precioMax != null) {
            sql.append(" AND precio <= ?");
            params.add(precioMax);
        }
        if (esVegetariana != null) {
            sql.append(" AND es_vegetariana = ?");
            params.add(esVegetariana);
        }

        sql.append(" ORDER BY nombre");
        return lanzarConsulta(sql.toString(), params.toArray());
    }

    private List<Comida> lanzarConsulta(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<Comida> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(crearComidaDesdeResultSet(rs));
                }
                return lista;
            }
        }
    }

    private Comida crearComidaDesdeResultSet(ResultSet rs) throws SQLException {
        Comida c = new Comida();
        c.setIdComida(rs.getInt("id_comida"));
        c.setNombre(rs.getString("nombre"));
        c.setTipo(rs.getString("tipo"));
        c.setPrecio(rs.getDouble("precio"));
        c.setStock(rs.getInt("stock"));
        c.setDescripcion(rs.getString("descripcion"));
        c.setEsVegetariana(rs.getBoolean("es_vegetariana"));
        c.setFechaRegistro(rs.getObject("fecha_registro", LocalDate.class));
        return c;
    }

    private void asignarParametros(PreparedStatement ps, Comida c, boolean incluirId) throws SQLException {
        ps.setString(1, c.getNombre());
        ps.setString(2, c.getTipo());
        ps.setDouble(3, c.getPrecio());
        ps.setInt(4, c.getStock());
        ps.setString(5, c.getDescripcion());
        ps.setBoolean(6, c.isEsVegetariana());
        ps.setDate(7, Date.valueOf(c.getFechaRegistro()));
        if (incluirId) {
            ps.setInt(8, c.getIdComida());
        }
    }
}
