// src/pages/contributions/ContributionsList.jsx
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
} from '@mui/material';
import { Add } from '@mui/icons-material';
import { contributionService } from '../../services/contributionService';

const ContributionsList = () => {
  const [contributions, setContributions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchContributions = async () => {
      try {
        const response = await contributionService.getContributions();
        const contributionsData = response.data || [];
        setContributions(Array.isArray(contributionsData) ? contributionsData : []);
      } catch (error) {
        console.error('Error fetching contributions:', error);
        setContributions([]);
      } finally {
        setLoading(false);
      }
    };

    fetchContributions();
  }, []);

  if (loading) {
    return (
      <Container>
        <Typography>Loading contributions...</Typography>
      </Container>
    );
  }

  return (
    <Container>
      <Box sx={{ mb: 3, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography variant="h4" component="h1">
          Contributions
        </Typography>
        <Button variant="contained" startIcon={<Add />}>
          Add Contribution
        </Button>
      </Box>

      <Paper>
        <TableContainer>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>Member</TableCell>
                <TableCell>Amount</TableCell>
                <TableCell>Status</TableCell>
                <TableCell>Date</TableCell>
                <TableCell>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {contributions.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={6} align="center">
                    No contributions found
                  </TableCell>
                </TableRow>
              ) : (
                contributions.map((contribution) => (
                  <TableRow key={contribution.id}>
                    <TableCell>{contribution.id}</TableCell>
                    <TableCell>{contribution.memberName || 'N/A'}</TableCell>
                    <TableCell>{contribution.amount || 'N/A'}</TableCell>
                    <TableCell>{contribution.status || 'N/A'}</TableCell>
                    <TableCell>{contribution.date || 'N/A'}</TableCell>
                    <TableCell>
                      <Button size="small">Edit</Button>
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

export default ContributionsList;