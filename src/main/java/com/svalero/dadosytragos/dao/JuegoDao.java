package com.svalero.dadosytragos.dao;

import com.svalero.dadosytragos.domain.Juego;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;

public class JuegoDao {
    private final Connection conexion;

    public JuegoDao(Connection conexion) {
        this.conexion = conexion;
    }

    public int agregar(Juego juego) throws SQLException {
        String sql = "INSERT INTO juegos (nombre, descripcion, categoria, min_jugadores, "
                + "max_jugadores, edad_minima, disponible, fecha_adquisicion, "
                + "valor_adquisicion, ruta_imagen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // carga todos los parámetros (sin id)
            asignarParametrosJuego(ps, juego, false);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se inserto ningún registro");
            }
            // Recupera la key generada
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idNuevo = rs.getInt(1);
                    juego.setIdJuego(idNuevo);
                    return idNuevo;
                } else {
                    throw new SQLException("No se pudo obtener el ID generado");
                }
            }
        }
    }



    public boolean actualizar(Juego juego) throws SQLException {
        String sql = "UPDATE juegos SET nombre=?, descripcion=?, categoria=?, min_jugadores=?, "
                + "max_jugadores=?, edad_minima=?, disponible=?, fecha_adquisicion=?, "
                + "valor_adquisicion=?, ruta_imagen=? WHERE id_juego=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            asignarParametrosJuego(ps, juego, true);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idJuego) throws SQLException {
        String sql = "DELETE FROM juegos WHERE id_juego = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idJuego);
            return ps.executeUpdate() > 0;
        }
    }

    public Juego buscarPorId(int idJuego) throws SQLException {
        String sql = "SELECT * FROM juegos WHERE id_juego = ?";
        List<Juego> lista = lanzarConsulta(sql, idJuego);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public List<Juego> listar(int limite, int desplazamiento) throws SQLException {
        String sql = "SELECT * FROM juegos LIMIT ? OFFSET ?";
        return lanzarConsulta(sql, limite, desplazamiento);
    }

    public int contarTodos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM juegos";
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public List<Juego> buscarPorCriterios(String nombre,
                                          String categoria,
                                          Integer minJugadores,
                                          Integer maxJugadores,
                                          Integer edadMinima,
                                          Boolean disponible) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM juegos WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (nombre != null && !nombre.isBlank()) {
            sql.append(" AND nombre LIKE ?");
            params.add("%" + nombre + "%");
        }
        if (categoria != null && !categoria.isBlank()) {
            sql.append(" AND categoria LIKE ?");
            params.add("%" + categoria + "%");
        }
        if (minJugadores != null) {
            sql.append(" AND min_jugadores >= ?");
            params.add(minJugadores);
        }
        if (maxJugadores != null) {
            sql.append(" AND max_jugadores <= ?");
            params.add(maxJugadores);
        }
        if (edadMinima != null) {
            sql.append(" AND edad_minima >= ?");
            params.add(edadMinima);
        }
        if (disponible != null) {
            sql.append(" AND disponible = ?");
            params.add(disponible);
        }

        // Opcional: orden
        sql.append(" ORDER BY nombre");

        // Reusa tu lanzarConsulta dinámica
        return lanzarConsulta(sql.toString(), params.toArray());
    }

    private List<Juego> lanzarConsulta(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<Juego> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(crearJuegoDesdeResultSet(rs));
                }
                return lista;
            }
        }
    }

    private Juego crearJuegoDesdeResultSet(ResultSet rs) throws SQLException {
        Juego j = new Juego();
        j.setIdJuego(rs.getInt("id_juego"));
        j.setNombre(rs.getString("nombre"));
        j.setDescripcion(rs.getString("descripcion"));
        j.setCategoria(rs.getString("categoria"));
        j.setMinJugadores(rs.getInt("min_jugadores"));
        j.setMaxJugadores(rs.getInt("max_jugadores"));
        j.setEdadMinima(rs.getInt("edad_minima"));
        j.setDisponible(rs.getBoolean("disponible"));
        java.sql.Date sqlDate = rs.getDate("fecha_adquisicion");
        j.setFechaAdquisicion(
                rs.getObject("fecha_adquisicion", java.time.LocalDate.class)
        );
        j.setValorAdquisicion(rs.getDouble("valor_adquisicion"));
        j.setRutaImagen(rs.getString("ruta_imagen"));
        return j;
    }

    private void asignarParametrosJuego(PreparedStatement ps, Juego j, boolean conId) throws SQLException {
        ps.setString(1, j.getNombre());
        ps.setString(2, j.getDescripcion());
        ps.setString(3, j.getCategoria());
        ps.setInt   (4, j.getMinJugadores());
        ps.setInt   (5, j.getMaxJugadores());
        ps.setInt   (6, j.getEdadMinima());
        ps.setBoolean(7, j.isDisponible());
        if (j.getFechaAdquisicion() != null) {
            ps.setDate(8, Date.valueOf(j.getFechaAdquisicion()));
        } else {
            ps.setNull(8, Types.DATE);
        }
        ps.setDouble(9, j.getValorAdquisicion());
        ps.setString(10, j.getRutaImagen());
        if (conId) {
            ps.setInt(11, j.getIdJuego());
        }
    }
}
