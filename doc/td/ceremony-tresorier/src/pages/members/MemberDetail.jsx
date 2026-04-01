// src/pages/members/MemberDetail.jsx
import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Container,
  Typography,
  Paper,
  Box,
  Grid,
  Avatar,
  Button,
  Chip,
} from '@mui/material';
import {
  Edit as EditIcon,
  ArrowBack as ArrowBackIcon,
} from '@mui/icons-material';
import { memberService } from '../../services/memberService';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import toast from 'react-hot-toast';

const MemberDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [member, setMember] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadMember();
  }, [id]);

  const loadMember = async () => {
    try {
      const response = await memberService.getMemberById(id);
      setMember(response.data);
    } catch (error) {
      console.error('Error loading member:', error);
      toast.error('Erreur lors du chargement du membre');
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = () => {
    navigate(`/members/edit/${id}`);
  };

  const handleBack = () => {
    navigate('/members');
  };

  if (loading) {
    return <LoadingSpinner message="Chargement du membre..." />;
  }

  if (!member) {
    return (
      <Container>
        <Typography>Membre non trouvé</Typography>
      </Container>
    );
  }

  return (
    <Container maxWidth="md">
      <Box sx={{ mb: 3, display: 'flex', alignItems: 'center', gap: 2 }}>
        <Button startIcon={<ArrowBackIcon />} onClick={handleBack}>
          Retour
        </Button>
        <Typography variant="h4" component="h1">
          Détails du Membre
        </Typography>
      </Box>

      <Paper sx={{ p: 3 }}>
        <Grid container spacing={3}>
          <Grid xs={12} md={4}>
            <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
              <Avatar sx={{ width: 120, height: 120, mb: 2 }}>
                {member.firstName?.[0]}{member.lastName?.[0]}
              </Avatar>
              <Chip
                label={member.active ? 'Actif' : 'Inactif'}
                color={member.active ? 'success' : 'default'}
              />
            </Box>
          </Grid>

          <Grid xs={12} md={8}>
            <Typography variant="h5" gutterBottom>
              {member.firstName} {member.lastName}
            </Typography>
            <Typography variant="body1" color="text.secondary" gutterBottom>
              N° Membre: {member.memberNumber || 'N/A'}
            </Typography>

            <Box sx={{ mt: 3 }}>
              <Typography variant="h6" gutterBottom>Informations de contact</Typography>
              <Typography><strong>Téléphone:</strong> {member.phoneNumber || 'N/A'}</Typography>
              {member.secondaryPhone && (
                <Typography><strong>Téléphone secondaire:</strong> {member.secondaryPhone}</Typography>
              )}
              <Typography><strong>Email:</strong> {member.email || 'N/A'}</Typography>
              <Typography><strong>Adresse:</strong> {member.address || 'N/A'}</Typography>
            </Box>

            {member.emergencyContact && (
              <Box sx={{ mt: 3 }}>
                <Typography variant="h6" gutterBottom>Contact d'urgence</Typography>
                <Typography><strong>Nom:</strong> {member.emergencyContact}</Typography>
                <Typography><strong>Téléphone:</strong> {member.emergencyPhone || 'N/A'}</Typography>
              </Box>
            )}

            {member.notes && (
              <Box sx={{ mt: 3 }}>
                <Typography variant="h6" gutterBottom>Notes</Typography>
                <Typography>{member.notes}</Typography>
              </Box>
            )}
          </Grid>
        </Grid>

        <Box sx={{ mt: 4, display: 'flex', justifyContent: 'flex-end' }}>
          <Button
            variant="contained"
            startIcon={<EditIcon />}
            onClick={handleEdit}
          >
            Modifier
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default MemberDetail;