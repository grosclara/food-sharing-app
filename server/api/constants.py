""" Define enumeration tuples """

GIF = 'Gif'
RENNES = 'Rennes'
METZ = 'Metz'

CAMPUS_CHOICES = [
        (GIF, 'Gif'),
        (RENNES, 'Rennes'),
        (METZ,'Metz')
]

AVAILABLE = 'Available'
COLLECTED = 'Collected'
DELIVERED = 'Delivered'

STATUS_CHOICES = [
        (AVAILABLE, 'Available'),
        (COLLECTED, 'Collected'),
        (DELIVERED,'Delivered')
]

FECULENTS = 'Féculents'
FRUITS_LEGUMES = 'Fruits/Légumes'
CONSERVES_PLATS_CUISINES = "Conserves/Plats cuisinés"
PRODUITS_LAITIERS = "Produits laitiers"
DESSERTS_PAIN = "Desserts/Pain"
VIANDES_OEUFS = 'Viandes/Oeufs'
PRODUITS_HYIGENE = "Produits d'hygiène"
PRODUITS_ENTRETIEN = "Produits d'entretien"
AUTRES_PRODUITS = "Autres"

PRODUCT_CHOICES = [
    (FECULENTS, 'Féculents'),
    (FRUITS_LEGUMES, 'Fruits/Légumes'),
    (CONSERVES_PLATS_CUISINES, 'Conserves/Plats cuisinés'),
    (PRODUITS_LAITIERS, 'Produits laitiers'),
    (DESSERTS_PAIN, 'Desserts/Pain'),
    (VIANDES_OEUFS, 'Viandes/Oeufs'),
    (PRODUITS_HYIGENE, "Produits d'hygiène"),
    (PRODUITS_ENTRETIEN, "Produits d'entretien"),
    (AUTRES_PRODUITS, 'Autres')
]