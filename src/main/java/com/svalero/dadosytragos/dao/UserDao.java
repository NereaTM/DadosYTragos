package com.svalero.dadosytragos.dao;

import com.svalero.dadosytragos.domain.User;
import com.svalero.dadosytragos.exception.UserNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    public User loginUser(String username, String password)
            throws SQLException, UserNotFoundException {
        String sql = "SELECT id_usuario, role FROM usuarios WHERE usuario = ? AND contrasena = SHA1(?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new UserNotFoundException();
                User u = new User();
                u.setIdUsuario  (rs.getInt   ("id_usuario"));
                u.setUsuario    (username);
                u.setRoleAdmin  (rs.getBoolean("role"));
                return u;
            }
        }
    }

    public int add(User user) throws SQLException {
        String sql = "INSERT INTO usuarios (usuario, contrasena, role, apellido, email, fecha_registro, fecha_nacimiento, edad, total_consumo) "
                + "VALUES (?, SHA1(?), ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsuario());
            ps.setString(2, user.getContrasena());
            ps.setBoolean(3, user.isRoleAdmin());
            ps.setString(4, user.getApellido());
            ps.setString(5, user.getEmail());
            if (user.getFechaRegistro() != null) {
                ps.setDate(6, Date.valueOf(user.getFechaRegistro()));
            } else {
                ps.setDate(6, Date.valueOf(LocalDate.now()));
            }
            if (user.getFechaNacimiento() != null) {
                ps.setDate(7, Date.valueOf(user.getFechaNacimiento()));
            } else {
                ps.setNull(7, Types.DATE);
            }
            if (user.getEdad() != null) {
                ps.setInt(8, user.getEdad());
            } else {
                ps.setNull(8, Types.INTEGER);
            }
            ps.setDouble(9, user.getTotalConsumo());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            throw new SQLException("No se obtuvo ID para el nuevo usuario");
        }
    }

    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setUsuario(rs.getString("usuario"));
                    u.setRoleAdmin(rs.getBoolean("role"));
                    u.setApellido(rs.getString("apellido"));
                    u.setEmail(rs.getString("email"));
                    Date reg = rs.getDate("fecha_registro");
                    if (reg != null) u.setFechaRegistro(reg.toLocalDate());
                    Date nac = rs.getDate("fecha_nacimiento");
                    if (nac != null) u.setFechaNacimiento(nac.toLocalDate());
                    u.setEdad(rs.getObject("edad", Integer.class));
                    u.setTotalConsumo(rs.getDouble("total_consumo"));
                    return u;
                }
                return null;
            }
        }
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM usuarios";
        List<User> list = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                User u = new User();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setUsuario(rs.getString("usuario"));
                u.setRoleAdmin(rs.getBoolean("role"));
                u.setApellido(rs.getString("apellido"));
                u.setEmail(rs.getString("email"));
                Date reg = rs.getDate("fecha_registro");
                if (reg != null) u.setFechaRegistro(reg.toLocalDate());
                Date nac = rs.getDate("fecha_nacimiento");
                if (nac != null) u.setFechaNacimiento(nac.toLocalDate());
                u.setEdad(rs.getObject("edad", Integer.class));
                u.setTotalConsumo(rs.getDouble("total_consumo"));
                list.add(u);
            }
        }
        return list;
    }

    public boolean update(User user) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "UPDATE usuarios SET usuario = ?, role = ?, apellido = ?, email = ?, fecha_nacimiento = ?, edad = ?, total_consumo = ?"
        );
        if (user.getContrasena() != null && !user.getContrasena().isBlank()) {
            sql.append(", contrasena = SHA1(?)");
        }
        sql.append(" WHERE id_usuario = ?");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int idx = 1;
            ps.setString(idx++, user.getUsuario());
            ps.setBoolean(idx++, user.isRoleAdmin());
            ps.setString(idx++, user.getApellido());
            ps.setString(idx++, user.getEmail());
            if (user.getFechaNacimiento() != null) {
                ps.setDate(idx++, Date.valueOf(user.getFechaNacimiento()));
            } else {
                ps.setNull(idx++, Types.DATE);
            }
            if (user.getEdad() != null) {
                ps.setInt(idx++, user.getEdad());
            } else {
                ps.setNull(idx++, Types.INTEGER);
            }
            ps.setDouble(idx++, user.getTotalConsumo());
            if (user.getContrasena() != null && !user.getContrasena().isBlank()) {
                ps.setString(idx++, user.getContrasena());
            }
            ps.setInt(idx, user.getIdUsuario());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
