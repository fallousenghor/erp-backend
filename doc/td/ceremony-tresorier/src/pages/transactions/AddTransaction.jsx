// src/pages/transactions/AddTransaction.jsx
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
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from '@mui/material';
import { ArrowBack as ArrowBackIcon } from '@mui/icons-material';
import { transactionService } from '../../services/transactionService';
import toast from 'react-hot-toast';

const AddTransaction = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    description: '',
    amount: '',
    type: 'ENTREE',
    category: '',
    transactionDate: new Date().toISOString().split('T')[0],
    paymentMethod: '',
    referenceNumber: '',
    memberId: '',
  });

  const handleInputChange = (field) => (event) => {
    setFormData(prev => ({
      ...prev,
      [field]: event.target.value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!formData.description || !formData.amount || !formData.type) {
      toast.error('Veuillez remplir tous les champs requis');
      return;
    }

    setLoading(true);
    try {
      await transactionService.createTransaction({
        ...formData,
        amount: parseFloat(formData.amount),
      });
      toast.success('Transaction créée avec succès');
      navigate('/transactions');
    } catch (error) {
      console.error('Error creating transaction:', error);
      toast.error('Erreur lors de la création de la transaction');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate('/transactions');
  };

  return (
    <Container maxWidth="md">
      <Box sx={{ mb: 3, display: 'flex', alignItems: 'center', gap: 2 }}>
        <Button startIcon={<ArrowBackIcon />} onClick={handleCancel}>
          Retour
        </Button>
        <Typography variant="h4" component="h1">
          Nouvelle Transaction
        </Typography>
      </Box>

      <Paper sx={{ p: 3 }}>
        <form onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Description"
                value={formData.description}
                onChange={handleInputChange('description')}
                required
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Montant"
                type="number"
                value={formData.amount}
                onChange={handleInputChange('amount')}
                required
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <FormControl fullWidth>
                <InputLabel>Type</InputLabel>
                <Select
                  value={formData.type}
                  label="Type"
                  onChange={handleInputChange('type')}
                  required
                >
                  <MenuItem value="ENTREE">Entrée</MenuItem>
                  <MenuItem value="SORTIE">Sortie</MenuItem>
                  <MenuItem value="COTISATION">Cotisation</MenuItem>
                  <MenuItem value="DON">Don</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Catégorie"
                value={formData.category}
                onChange={handleInputChange('category')}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Date"
                type="date"
                value={formData.transactionDate}
                onChange={handleInputChange('transactionDate')}
                InputLabelProps={{ shrink: true }}
                required
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Méthode de paiement"
                value={formData.paymentMethod}
                onChange={handleInputChange('paymentMethod')}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Numéro de référence"
                value={formData.referenceNumber}
                onChange={handleInputChange('referenceNumber')}
              />
            </Grid>
          </Grid>

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
              {loading ? 'Création...' : 'Créer la transaction'}
            </Button>
          </Box>
        </form>
      </Paper>
    </Container>
  );
};

export default AddTransaction;