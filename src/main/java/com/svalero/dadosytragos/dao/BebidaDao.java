package com.svalero.dadosytragos.dao;

import com.svalero.dadosytragos.domain.Bebida;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BebidaDao {
    private final Connection conexion;

    public BebidaDao(Connection conexion) {
        this.conexion = conexion;
    }

    public int agregar(Bebida bebida) throws SQLException {
        String sql = "INSERT INTO bebidas (nombre, tipo, precio, stock, graduacion_alcoholica, es_alcoholica, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            asignarParametros(ps, bebida, false);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se inserto ninguna bebida");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    bebida.setIdBebida(rs.getInt(1));
                    return bebida.getIdBebida();
                } else {
                    throw new SQLException("No se genero el ID");
                }
            }
        }
    }

    public boolean actualizar(Bebida bebida) throws SQLException {
        String sql = "UPDATE bebidas SET nombre=?, tipo=?, precio=?, stock=?, graduacion_alcoholica=?, es_alcoholica=?, fecha_registro=? WHERE id_bebida=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            asignarParametros(ps, bebida, true);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idBebida) throws SQLException {
        String sql = "DELETE FROM bebidas WHERE id_bebida = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idBebida);
            return ps.executeUpdate() > 0;
        }
    }

    public Bebida buscarPorId(int idBebida) throws SQLException {
        String sql = "SELECT * FROM bebidas WHERE id_bebida = ?";
        List<Bebida> lista = lanzarConsulta(sql, idBebida);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public List<Bebida> listarTodas() throws SQLException {
        String sql = "SELECT * FROM bebidas";
        return lanzarConsulta(sql);
    }

    public List<Bebida> buscarPorCriterios(String nombre, String tipo, Double precioMin,
                                           Double precioMax, Boolean esAlcoholica,
                                           Double graduacionMin, Double graduacionMax) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM bebidas WHERE 1=1");
        List<Object> parametros = new ArrayList<>();

        if (nombre != null && !nombre.isBlank()) {
            sql.append(" AND LOWER(nombre) LIKE LOWER(?)");
            parametros.add("%" + nombre + "%");
        }
        if (tipo != null && !tipo.isBlank()) {
            sql.append(" AND LOWER(tipo) LIKE LOWER(?)");
            parametros.add("%" + tipo + "%");
        }
        if (precioMin != null) {
            sql.append(" AND precio >= ?");
            parametros.add(precioMin);
        }
        if (precioMax != null) {
            sql.append(" AND precio <= ?");
            parametros.add(precioMax);
        }
        if (esAlcoholica != null) {
            sql.append(" AND es_alcoholica = ?");
            parametros.add(esAlcoholica);
        }
        if (graduacionMin != null) {
            sql.append(" AND graduacion_alcoholica >= ?");
            parametros.add(graduacionMin);
        }
        if (graduacionMax != null) {
            sql.append(" AND graduacion_alcoholica <= ?");
            parametros.add(graduacionMax);
        }

        sql.append(" ORDER BY nombre");
        return lanzarConsulta(sql.toString(), parametros.toArray());
    }

    private List<Bebida> lanzarConsulta(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++)
                ps.setObject(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                List<Bebida> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(crearBebidaDesdeResultSet(rs));
                }
                return lista;
            }
        }
    }

    private Bebida crearBebidaDesdeResultSet(ResultSet rs) throws SQLException {
        Bebida bebida = new Bebida();
        bebida.setIdBebida(rs.getInt("id_bebida"));
        bebida.setNombre(rs.getString("nombre"));
        bebida.setTipo(rs.getString("tipo"));
        bebida.setPrecio(rs.getDouble("precio"));
        bebida.setStock(rs.getInt("stock"));
        bebida.setGraduacionAlcoholica(rs.getDouble("graduacion_alcoholica"));
        bebida.setEsAlcoholica(rs.getBoolean("es_alcoholica"));
        bebida.setFechaRegistro(rs.getObject("fecha_registro", LocalDate.class));
        return bebida;
    }

    private void asignarParametros(PreparedStatement ps, Bebida bebida, boolean incluirId) throws SQLException {
        ps.setString(1, bebida.getNombre());
        ps.setString(2, bebida.getTipo());
        ps.setDouble(3, bebida.getPrecio());
        ps.setInt(4, bebida.getStock());
        ps.setDouble(5, bebida.getGraduacionAlcoholica());
        ps.setBoolean(6, bebida.isEsAlcoholica());
        ps.setDate(7, Date.valueOf(bebida.getFechaRegistro()));
        if (incluirId)
            ps.setInt(8, bebida.getIdBebida());
    }
}
