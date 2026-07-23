# File Format

## Header

| Offset | Length | Type         | Meaning             |
|--------|--------|--------------|---------------------|
| 0      | 4      | UINT32       | File Format Version |
| 4      | 4      | UINT32       | Reserved            |
| 8      | ?      | SCOPE        | Global Scope        |


## Scope

| Offset | Length | Type         | Meaning            |
|--------|--------|--------------|--------------------|
| 0      | 4      | UINT32       | Scope ID           |
| 4      | 4      | UINT32       | Parent Scope ID    |
| 8      | 4      | UINT32       | Reserved           |
| 12     | 4      | UINT32       | Action List Length |
| 16     | ?      | ACTION ARRAY | Action List        |

## Action

| Offset | Length | Type   | Meaning              |
|--------|--------|--------|----------------------|
| 0      | 4      | UINT32 | Action Type          |
| 4      | ?      | ?      | Specific Action Data |

### Boolean Action
| Offset | Length | Type   | Meaning       |
|--------|--------|--------|---------------|
| 0      | 4      | UINT32 | 1 (Type)      |
| 4      | 4      | UINT32 | Boolean Value |

### Call Function Action
| Offset | Length | Type         | Meaning                    |
|--------|--------|--------------|----------------------------|
| 0      | 4      | UINT32       | 2 (Type)                   |
| 4      | 4      | UINT32       | Function Name String Index |
| 8      | 4      | UINT32       | Reserved                   |
| 12     | 4      | UINT32       | Argument Count             |
| 16     | ?      | ACTION ARRAY | Arguments                  |

### Else Action
| Offset | Length | Type   | Meaning  |
|--------|--------|--------|----------|
| 0      | 4      | UINT32 | 3 (Type) |
| 4      | 4      | UINT32 | Reserved |
| 8      | ?      | SCOPE  | Scope    |

### Execute On Other Scope Action
| Offset | Length | Type   | Meaning                    |
|--------|--------|--------|----------------------------|
| 0      | 4      | UINT32 | 4 (Type)                   |
| 4      | 4      | UINT32 | Variable Name String Index |
| 8      | ?      | ACTION | Action                     |

### Get Variable Action
| Offset | Length | Type   | Meaning                    |
|--------|--------|--------|----------------------------|
| 0      | 4      | UINT32 | 5 (Type)                   |
| 4      | 4      | UINT32 | Variable Name String Index |

### If Action
| Offset | Length | Type        | Meaning     |
|--------|--------|-------------|-------------|
| 0      | 4      | UINT32      | 6 (Type)    |
| 4      | 4      | UINT32      | Reserved    |
| 8      | ?      | ACTION      | Condition   |
| ?      | ?      | ELSE ACTION | Else Action |
| ?      | ?      | SCOPE       | Scope       |

### Import Action
| Offset | Length | Type   | Meaning                |
|--------|--------|--------|------------------------|
| 0      | 4      | UINT32 | 7 (Type)               |
| 4      | 4      | UINT32 | File Name String Index |

### Intern Action
| Offset | Length | Type   | Meaning  |
|--------|--------|--------|----------|
| 0      | 4      | UINT32 | 8 (Type) |
| 4      | 4      | UINT32 | Reserved |
| 8      | ?      | ACTION | Action   |

### Math Action
| Offset | Length | Type   | Meaning      |
|--------|--------|--------|--------------|
| 0      | 4      | UINT32 | 9 (Type)     |
| 4      | 4      | STRING | Math Symbol  |
| 8      | ?      | ACTION | Action Left  |
| ?      | ?      | ACTION | Action Right |

### New Class Object Action
| Offset | Length | Type         | Meaning                 |
|--------|--------|--------------|-------------------------|
| 0      | 4      | UINT32       | 10 (Type)               |
| 4      | 4      | UINT32       | Class Name String Index |
| 8      | 4      | UINT32       | Reserved                |
| 12     | 4      | UINT32       | Argument Count          |
| 16     | ?      | ACTION ARRAY | Arguments               |

### Number Action
| Offset | Length | Type   | Meaning   |
|--------|--------|--------|-----------|
| 0      | 4      | UINT32 | 11 (Type) |
| 4      | 4      | UINT32 | Reserved  |
| 8      | 8      | UINT64 | Value     |

### Return Action
| Offset | Length | Type   | Meaning   |
|--------|--------|--------|-----------|
| 0      | 4      | UINT32 | 12 (Type) |
| 4      | 4      | UINT32 | Reserved  |
| 8      | ?      | ACTION | Action    |

### Run Action
| Offset | Length | Type   | Meaning   |
|--------|--------|--------|-----------|
| 0      | 4      | UINT32 | 13 (Type) |
| 4      | 4      | UINT32 | Reserved  |
| 8      | ?      | ACTION | Action    |

### Set Variable Action
| Offset | Length | Type   | Meaning                    |
|--------|--------|--------|----------------------------|
| 0      | 4      | UINT32 | 14 (Type)                  |
| 4      | 4      | UINT32 | Variable Name String Index |
| 8      | ?      | ACTION | Action                     |

### String Action
| Offset | Length | Type   | Meaning            |
|--------|--------|--------|--------------------|
| 0      | 4      | UINT32 | 15 (Type)          |
| 4      | 4      | UINT32 | String Value Index |

### Variable Declaration Action
| Offset | Length | Type   | Meaning         |
|--------|--------|--------|-----------------|
| 0      | 4      | UINT32 | 16 (Type)       |
| 4      | 4      | UINT32 | Has Start Value |
| 8      | 4      | UINT32 | Padding         |
| 12     | ?      | STRING | Variable Name   |
| ?      | ?      | ACTION | Action          |

### Function
| Offset | Length | Type         | Meaning                    |
|--------|--------|--------------|----------------------------|
| 0      | 4      | UINT32       | 17 (Type)                  |
| 4      | 4      | UINT32       | Function Name String Index |
| 8      | 4      | UINT32       | Padding                    |
| 12     | 4      | UINT32       | Argument Size              |
| 16     | ?      | ACTION ARRAY | Arguments                  |
| ?      | ?      | SCOPE        | Scope                      |


### Class
| Offset | Length | Type   | Meaning                 |
|--------|--------|--------|-------------------------|
| 0      | 4      | UINT32 | 18 (Type)               |
| 4      | 4      | UINT32 | Class Name String Index |
| 8      | ?      | SCOPE  | Scope                   |

### Array Action
| Offset | Length | Type   | Meaning   |
|--------|--------|--------|-----------|
| 0      | 4      | UINT32 | 19 (Type) |
| 4      | 4      | UINT32 | Reserved  |
| 8      | ?      | ACTION | Action    |

### For Action
| Offset | Length | Type   | Meaning        |
|--------|--------|--------|----------------|
| 0      | 4      | UINT32 | 20 (Type)      |
| 4      | 4      | UINT32 | Reserved       |
| 8      | ?      | ACTION | Loop Start     |
| ?      | ?      | ACTION | Condition      |
| ?      | ?      | ACTION | Loop Iteration |
| ?      | ?      | SCOPE  | Scope          |