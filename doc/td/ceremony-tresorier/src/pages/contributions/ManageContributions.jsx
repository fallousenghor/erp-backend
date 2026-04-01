// src/pages/contributions/ManageContributions.jsx
import { useState, useEffect } from 'react';
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
  Chip,
} from '@mui/material';
import { contributionService } from '../../services/contributionService';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import toast from 'react-hot-toast';

const ManageContributions = () => {
  const [contributions, setContributions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadContributions();
  }, []);

  const loadContributions = async () => {
    try {
      const response = await contributionService.getContributions();
      const contributionsData = response.data || [];
      setContributions(Array.isArray(contributionsData) ? contributionsData : []);
    } catch (error) {
      console.error('Error loading contributions:', error);
      toast.error('Erreur lors du chargement des contributions');
      setContributions([]);
    } finally {
      setLoading(false);
    }
  };

  const handleMarkAsPaid = async (contributionId) => {
    try {
      // This would need a transaction ID, for now just show a message
      toast.info('Fonctionnalité à implémenter avec sélection de transaction');
    } catch (error) {
      console.error('Error marking as paid:', error);
      toast.error('Erreur lors du marquage comme payé');
    }
  };

  if (loading) {
    return <LoadingSpinner message="Chargement des contributions..." />;
  }

  return (
    <Container maxWidth="xl">
      <Typography variant="h4" component="h1" gutterBottom>
        Gestion des Contributions
      </Typography>

      <Paper sx={{ p: 2 }}>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>Membre</TableCell>
                <TableCell>Montant</TableCell>
                <TableCell>Statut</TableCell>
                <TableCell>Date</TableCell>
                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {contributions.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={6} align="center">
                    Aucune contribution trouvée
                  </TableCell>
                </TableRow>
              ) : (
                contributions.map((contribution) => (
                  <TableRow key={contribution.id}>
                    <TableCell>{contribution.id}</TableCell>
                    <TableCell>{contribution.memberName || 'N/A'}</TableCell>
                    <TableCell>{contribution.amount || 'N/A'}</TableCell>
                    <TableCell>
                      <Chip
                        label={contribution.status || 'Non payé'}
                        color={contribution.status === 'PAYE' ? 'success' : 'warning'}
                        size="small"
                      />
                    </TableCell>
                    <TableCell>{contribution.date || 'N/A'}</TableCell>
                    <TableCell>
                      {contribution.status !== 'PAYE' && (
                        <Button
                          size="small"
                          variant="outlined"
                          onClick={() => handleMarkAsPaid(contribution.id)}
                        >
                          Marquer payé
                        </Button>
                      )}
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

export default ManageContributions;