// src/pages/members/MembersList.jsx
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Container,
  Typography,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Button,
  Box,
  TextField,
  InputAdornment,
  Chip,
  IconButton,
} from '@mui/material';
import {
  Add as AddIcon,
  Search as SearchIcon,
  Visibility as VisibilityIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
} from '@mui/icons-material';
import { memberService } from '../../services/memberService';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import toast from 'react-hot-toast';

const MembersList = () => {
  const [members, setMembers] = useState([]);
  const [filteredMembers, setFilteredMembers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    loadMembers();
  }, []);

  useEffect(() => {
    const filtered = members.filter(member =>
      `${member.firstName} ${member.lastName}`.toLowerCase().includes(searchTerm.toLowerCase()) ||
      member.phoneNumber?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      member.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      member.memberNumber?.toString().includes(searchTerm)
    );
    setFilteredMembers(filtered);
  }, [members, searchTerm]);

  const loadMembers = async () => {
    try {
      const response = await memberService.getMembers();
      const membersData = response.data || [];
      setMembers(Array.isArray(membersData) ? membersData : []);
    } catch (error) {
      console.error('Error loading members:', error);
      toast.error('Erreur lors du chargement des membres');
      setMembers([]);
    } finally {
      setLoading(false);
    }
  };

  const handleViewMember = (member) => {
    navigate(`/members/${member.id}`);
  };

  const handleEditMember = (member) => {
    navigate(`/members/edit/${member.id}`);
  };

  const handleDeleteMember = async (member) => {
    if (window.confirm(`Supprimer le membre ${member.firstName} ${member.lastName} ?`)) {
      try {
        await memberService.deleteMember(member.id);
        toast.success('Membre supprimé avec succès');
        loadMembers();
      } catch (error) {
        console.error('Error deleting member:', error);
        toast.error('Erreur lors de la suppression');
      }
    }
  };

  const handleAddMember = () => {
    navigate('/members/new');
  };

  if (loading) {
    return <LoadingSpinner message="Chargement des membres..." />;
  }

  return (
    <Container maxWidth="xl">
      <Box sx={{ mb: 3, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography variant="h4" component="h1">
          Gestion des Membres
        </Typography>
        <Button variant="contained" startIcon={<AddIcon />} onClick={handleAddMember}>
          Nouveau Membre
        </Button>
      </Box>

      <Paper sx={{ p: 2, mb: 2 }}>
        <TextField
          fullWidth
          variant="outlined"
          placeholder="Rechercher par nom, téléphone, email..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon />
              </InputAdornment>
            ),
          }}
          sx={{ mb: 2 }}
        />

        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>N° Membre</TableCell>
                <TableCell>Nom Complet</TableCell>
                <TableCell>Téléphone</TableCell>
                <TableCell>Email</TableCell>
                <TableCell>Statut</TableCell>
                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredMembers.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={6} align="center">
                    Aucun membre trouvé
                  </TableCell>
                </TableRow>
              ) : (
                filteredMembers.map((member) => (
                  <TableRow key={member.id}>
                    <TableCell>{member.memberNumber || 'N/A'}</TableCell>
                    <TableCell>{`${member.firstName} ${member.lastName}`}</TableCell>
                    <TableCell>{member.phoneNumber || '-'}</TableCell>
                    <TableCell>{member.email || '-'}</TableCell>
                    <TableCell>
                      <Chip
                        label={member.active ? 'Actif' : 'Inactif'}
                        color={member.active ? 'primary' : 'default'}
                        size="small"
                      />
                    </TableCell>
                    <TableCell>
                      <IconButton
                        size="small"
                        onClick={() => handleViewMember(member)}
                        title="Voir"
                      >
                        <VisibilityIcon />
                      </IconButton>
                      <IconButton
                        size="small"
                        onClick={() => handleEditMember(member)}
                        title="Modifier"
                      >
                        <EditIcon />
                      </IconButton>
                      <IconButton
                        size="small"
                        color="error"
                        onClick={() => handleDeleteMember(member)}
                        title="Supprimer"
                      >
                        <DeleteIcon />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
    </Container>
  );
};

export default MembersList;