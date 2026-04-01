// src/pages/members/AddMember.jsx
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Container,
  Typography,
  Paper,
  TextField,
  Button,
  Box,
  Grid,
  Avatar,
  IconButton,
} from '@mui/material';
import {
  Person as PersonIcon,
  Phone as PhoneIcon,
  Email as EmailIcon,
  Home as HomeIcon,
  ContactEmergency as ContactEmergencyIcon,
  PhotoCamera as PhotoCameraIcon,
  ArrowBack as ArrowBackIcon,
} from '@mui/icons-material';
import { memberService } from '../../services/memberService';
import toast from 'react-hot-toast';

const AddMember = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [photoPreview, setPhotoPreview] = useState(null);
  const [selectedFile, setSelectedFile] = useState(null);
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    secondaryPhone: '',
    dateOfBirth: '',
    address: '',
    emergencyContact: '',
    emergencyPhone: '',
    notes: '',
  });

  const handleInputChange = (field) => (event) => {
    setFormData(prev => ({
      ...prev,
      [field]: event.target.value,
    }));
  };

  const handleFileSelect = (event) => {
    const file = event.target.files[0];
    if (file) {
      setSelectedFile(file);
      const reader = new FileReader();
      reader.onload = (e) => {
        setPhotoPreview(e.target.result);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    // Basic validation
    if (!formData.firstName || !formData.lastName || !formData.phoneNumber) {
      toast.error('Veuillez remplir tous les champs requis');
      return;
    }

    setLoading(true);
    try {
      await memberService.createMember(formData);
      toast.success('Membre créé avec succès');
      navigate('/members');
    } catch (error) {
      console.error('Error creating member:', error);
      toast.error('Erreur lors de la création du membre');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate('/members');
  };

  return (
    <Container maxWidth="md">
      <Box sx={{ mb: 3, display: 'flex', alignItems: 'center', gap: 2 }}>
        <IconButton onClick={handleCancel}>
          <ArrowBackIcon />
        </IconButton>
        <Typography variant="h4" component="h1">
          Nouveau Membre
        </Typography>
      </Box>

      <Paper sx={{ p: 3 }}>
        <form onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            {/* Photo Section */}
            <Grid xs={12} md={4}>
              <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 2 }}>
                <Avatar
                  src={photoPreview}
                  sx={{ width: 120, height: 120 }}
                >
                  <PersonIcon sx={{ fontSize: 60 }} />
                </Avatar>
                <input
                  accept="image/*"
                  style={{ display: 'none' }}
                  id="photo-upload"
                  type="file"
                  onChange={handleFileSelect}
                />
                <label htmlFor="photo-upload">
                  <Button
                    variant="outlined"
                    component="span"
                    startIcon={<PhotoCameraIcon />}
                  >
                    Changer la photo
                  </Button>
                </label>
              </Box>
            </Grid>

            {/* Form Fields */}
            <Grid xs={12} md={8}>
              <Typography variant="h6" gutterBottom>
                Informations Personnelles
              </Typography>
              <Grid container spacing={2}>
                <Grid xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Prénom"
                    value={formData.firstName}
                    onChange={handleInputChange('firstName')}
                    required
                    InputProps={{
                      startAdornment: <PersonIcon sx={{ mr: 1, color: 'action.active' }} />,
                    }}
                  />
                </Grid>
                <Grid xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Nom"
                    value={formData.lastName}
                    onChange={handleInputChange('lastName')}
                    required
                    InputProps={{
                      startAdornment: <PersonIcon sx={{ mr: 1, color: 'action.active' }} />,
                    }}
                  />
                </Grid>
                <Grid xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Date de naissance"
                    type="date"
                    value={formData.dateOfBirth}
                    onChange={handleInputChange('dateOfBirth')}
                    InputLabelProps={{ shrink: true }}
                  />
                </Grid>
                <Grid xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Email"
                    type="email"
                    value={formData.email}
                    onChange={handleInputChange('email')}
                    InputProps={{
                      startAdornment: <EmailIcon sx={{ mr: 1, color: 'action.active' }} />,
                    }}
                  />
                </Grid>
              </Grid>

              <Typography variant="h6" sx={{ mt: 3, mb: 2 }}>
                Contact
              </Typography>
              <Grid container spacing={2}>
                <Grid xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Téléphone"
                    value={formData.phoneNumber}
                    onChange={handleInputChange('phoneNumber')}
                    required
                    placeholder="221771234567"
                    InputProps={{
                      startAdornment: <PhoneIcon sx={{ mr: 1, color: 'action.active' }} />,
                    }}
                  />
                </Grid>
                <Grid xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Téléphone secondaire"
                    value={formData.secondaryPhone}
                    onChange={handleInputChange('secondaryPhone')}
                    InputProps={{
                      startAdornment: <PhoneIcon sx={{ mr: 1, color: 'action.active' }} />,
                    }}
                  />
                </Grid>
                <Grid xs={12}>
                  <TextField
                    fullWidth
                    label="Adresse"
                    multiline
                    rows={3}
                    value={formData.address}
                    onChange={handleInputChange('address')}
                    InputProps={{
                      startAdornment: <HomeIcon sx={{ mr: 1, mt: 1, color: 'action.active' }} />,
                    }}
                  />
                </Grid>
              </Grid>

              <Typography variant="h6" sx={{ mt: 3, mb: 2 }}>
                Contact d'Urgence
              </Typography>
              <Grid container spacing={2}>
                <Grid xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Nom du contact"
                    value={formData.emergencyContact}
                    onChange={handleInputChange('emergencyContact')}
                    InputProps={{
                      startAdornment: <ContactEmergencyIcon sx={{ mr: 1, color: 'action.active' }} />,
                    }}
                  />
                </Grid>
                <Grid xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Téléphone d'urgence"
                    value={formData.emergencyPhone}
                    onChange={handleInputChange('emergencyPhone')}
                    InputProps={{
                      startAdornment: <PhoneIcon sx={{ mr: 1, color: 'action.active' }} />,
                    }}
                  />
                </Grid>
              </Grid>

              <Typography variant="h6" sx={{ mt: 3, mb: 2 }}>
                Notes
              </Typography>
              <TextField
                fullWidth
                label="Notes additionnelles"
                multiline
                rows={4}
                value={formData.notes}
                onChange={handleInputChange('notes')}
              />
            </Grid>
          </Grid>

          {/* Actions */}
          <Box sx={{ mt: 4, display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
            <Button
              variant="outlined"
              onClick={handleCancel}
              disabled={loading}
            >
              Annuler
            </Button>
            <Button
              type="submit"
              variant="contained"
              disabled={loading}
            >
              {loading ? 'Création...' : 'Créer le membre'}
            </Button>
          </Box>
        </form>
      </Paper>
    </Container>
  );
};

export default AddMember;